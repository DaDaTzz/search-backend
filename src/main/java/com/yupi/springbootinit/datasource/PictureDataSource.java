package com.yupi.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.dto.picture.PictureQueryRequest;
import com.yupi.springbootinit.model.vo.PictureVO;
import com.yupi.springbootinit.service.PictureService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class PictureDataSource implements DataSource<PictureVO>{

    @Resource
    private PictureService pictureService;
    @Override
    public Page<PictureVO> doSearch(String searchText, long current, long pageSize, HttpServletRequest request) {
        PictureQueryRequest pictureQueryRequest = new PictureQueryRequest();
        pictureQueryRequest.setSearchText(searchText);
        pictureQueryRequest.setCurrent((int) current);
        pictureQueryRequest.setPageSize((int) pageSize);
        Page<PictureVO> pictureVOPage = pictureService.grabPictureFromBing(pictureQueryRequest);
        return pictureVOPage;
    }
}
