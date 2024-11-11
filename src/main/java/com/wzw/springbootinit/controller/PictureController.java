package com.wzw.springbootinit.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzw.springbootinit.annotation.AuthCheck;
import com.wzw.springbootinit.common.BaseResponse;
import com.wzw.springbootinit.common.DeleteRequest;
import com.wzw.springbootinit.common.ErrorCode;
import com.wzw.springbootinit.common.ResultUtils;
import com.wzw.springbootinit.constant.UserConstant;
import com.wzw.springbootinit.exception.BusinessException;
import com.wzw.springbootinit.exception.ThrowUtils;
import com.wzw.springbootinit.model.dto.picture.PictureQueryRequest;
import com.wzw.springbootinit.model.dto.post.PostAddRequest;
import com.wzw.springbootinit.model.dto.post.PostEditRequest;
import com.wzw.springbootinit.model.dto.post.PostQueryRequest;
import com.wzw.springbootinit.model.dto.post.PostUpdateRequest;
import com.wzw.springbootinit.model.entity.Picture;
import com.wzw.springbootinit.model.entity.Post;
import com.wzw.springbootinit.model.entity.User;
import com.wzw.springbootinit.model.vo.PostVO;
import com.wzw.springbootinit.service.PictureService;
import com.wzw.springbootinit.service.PostService;
import com.wzw.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;

    /**
     * 分页获取列表（封装类）
     *
     * @param pictureQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPostVOByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                        HttpServletRequest request) {
        if(pictureQueryRequest == null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String searchText = pictureQueryRequest.getSearchText();
        long current = pictureQueryRequest.getCurrent();
        long size = pictureQueryRequest.getPageSize();
        // 限制爬虫
        Page<Picture> picturePage = pictureService.getPictureListPage(searchText,current,size);
        return ResultUtils.success(picturePage);
    }



}
