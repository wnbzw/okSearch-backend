package com.wzw.springbootinit.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzw.springbootinit.common.ErrorCode;
import com.wzw.springbootinit.datasource.*;
import com.wzw.springbootinit.exception.BusinessException;
import com.wzw.springbootinit.exception.ThrowUtils;
import com.wzw.springbootinit.model.dto.post.PostQueryRequest;
import com.wzw.springbootinit.model.dto.search.SearchQueryRequest;
import com.wzw.springbootinit.model.dto.user.UserQueryRequest;
import com.wzw.springbootinit.model.entity.Picture;
import com.wzw.springbootinit.model.enums.SearchTypeEnum;
import com.wzw.springbootinit.model.vo.PostVO;
import com.wzw.springbootinit.model.vo.SearchVO;
import com.wzw.springbootinit.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/*
 * 搜索门面
 */
@Component
@Slf4j
public class SearchFacade {
    @Resource
    private UserDataSource userDataSource;
    @Resource
    private PostDataSource postDataSource;
    @Resource
    private DataSourceRegistry dataSourceRegistry;
    @Resource
    private PictureDataSource pictureDataSource;

    public SearchVO doSearch(@RequestBody SearchQueryRequest searchRequest, HttpServletRequest request) {
        String type=searchRequest.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        ThrowUtils.throwIf(searchTypeEnum==null, ErrorCode.PARAMS_ERROR);
        String searchText = searchRequest.getSearchText();
        long current = searchRequest.getCurrent();
        long pageSize = searchRequest.getPageSize();

        //搜索所有的数据
        if(searchTypeEnum==null){
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                Page<UserVO> userVOPage = userDataSource.doSearch(searchText, current, pageSize);
                return userVOPage;
            });
            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setSearchText(searchText);
                Page<PostVO> postVOPage = postDataSource.doSearch(searchText, current, pageSize);
                return postVOPage;
            });
            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
                Page<Picture> picturePage = pictureDataSource.doSearch(searchText, 1, 10);
                return picturePage;
            });
            CompletableFuture.allOf(userTask, postTask, pictureTask).join();
            try {
                Page<UserVO> userVOPage = userTask.get();
                Page<PostVO> postVOPage = postTask.get();
                Page<Picture> picturePage = pictureTask.get();
                SearchVO searchVO = new SearchVO();
                searchVO.setUserList(userVOPage.getRecords());
                searchVO.setPostList(postVOPage.getRecords());
                searchVO.setPictureList(picturePage.getRecords());
                return searchVO;
            } catch (Exception e) {
                log.error("查询异常", e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
            }
        }else{
            SearchVO searchVO = new SearchVO();
            DataSource dataSource = dataSourceRegistry.getDataSourceByType(type);
            Page page = dataSource.doSearch(searchText, current, pageSize);
            searchVO.setDataList(page.getRecords());
            return searchVO;
        }
    }
}
