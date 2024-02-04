package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.datasource.DataSource;
import com.yupi.springbootinit.datasource.PictureDataSource;
import com.yupi.springbootinit.datasource.PostDataSource;
import com.yupi.springbootinit.datasource.UserDataSource;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.search.SearchRequest;
import com.yupi.springbootinit.model.vo.PictureVO;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.model.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Resource
    private PostDataSource postDataSource;
    @Resource
    private UserDataSource userDataSource;
    @Resource
    private PictureDataSource pictureDataSource;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest,
                                                     HttpServletRequest request) {
        long current = searchRequest.getCurrent();
        long size = searchRequest.getPageSize();
        String type = searchRequest.getType();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        String searchText = searchRequest.getSearchText();
        if (StringUtils.isBlank(type)) {
            Page<PostVO> postVOPage = postDataSource.doSearch(searchText, current, size, request);
            Page<UserVO> userVOPage = userDataSource.doSearch(searchText, current, size, request);
            Page<PictureVO> pictureVOPage = pictureDataSource.doSearch(searchText, current, size, request);
            SearchVO searchVO = new SearchVO();
            searchVO.setPostVOList(postVOPage.getRecords());
            searchVO.setUserVOList(userVOPage.getRecords());
            searchVO.setPictureVOList(pictureVOPage.getRecords());
            return ResultUtils.success(searchVO);
        } else {
            Map<String, DataSource> typeDataSourceMap = new HashMap(){{
                put("user", userDataSource);
                put("post", postDataSource);
                put("picture", pictureDataSource);
            }};
            Page page = typeDataSourceMap.get(type).doSearch(searchText, current, size, request);
            SearchVO searchVO = new SearchVO();
            searchVO.setDataList(page.getRecords());
            return ResultUtils.success(searchVO);
        }

    }

}
