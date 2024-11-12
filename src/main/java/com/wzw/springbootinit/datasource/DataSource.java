package com.wzw.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/*
 * 数据源接口(接入的数据源必须实现)
 *
 */
public interface DataSource<T> {

    /**
     * 搜索
     *
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<T> doSearch(String searchText, long pageNum, long pageSize);
}
