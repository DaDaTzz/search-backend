package com.yupi.springbootinit.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class SearchVO {

    private List<UserVO> userVOList;
    private List<PostVO> postVOList;
    private List<PictureVO> pictureVOList;
    private List<Object> dataList;

    private static final long serialVersionUID = 1L;

}
