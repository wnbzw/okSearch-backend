package com.wzw.springbootinit.controller;


import com.wzw.springbootinit.common.BaseResponse;
import com.wzw.springbootinit.common.ResultUtils;
import com.wzw.springbootinit.manager.SearchFacade;
import com.wzw.springbootinit.model.dto.search.SearchQueryRequest;
import com.wzw.springbootinit.model.vo.SearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 帖子接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private SearchFacade searchFacade;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchQueryRequest searchQueryRequest,
                                            HttpServletRequest request) {
        return ResultUtils.success(searchFacade.doSearch(searchQueryRequest, request));
    }
}
