package com.yupi.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.http.HttpServletRequest;

public interface DataSource<T> {

    Page<T> doSearch(String searchText, long current, long pageSize, HttpServletRequest request);
}
