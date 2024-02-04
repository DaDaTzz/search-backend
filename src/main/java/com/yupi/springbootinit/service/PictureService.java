package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.dto.picture.PictureQueryRequest;
import com.yupi.springbootinit.model.vo.PictureVO;

/**
 * 图片服务
 *
 */
public interface PictureService {

    Page<PictureVO> grabPictureFromBing(PictureQueryRequest pictureQueryRequest);
}
