package com.wzw.springbootinit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzw.springbootinit.model.entity.Picture;

/**
 * 帖子服务
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface PictureService  {


    /**
     * 分页获取帖子封装
     * @return
     */
    Page<Picture> getPictureListPage(String searchText, long current, long size);
}
