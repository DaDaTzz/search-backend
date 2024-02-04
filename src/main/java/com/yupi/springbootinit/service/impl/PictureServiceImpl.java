package com.yupi.springbootinit.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.dto.picture.PictureQueryRequest;
import com.yupi.springbootinit.model.vo.PictureVO;
import com.yupi.springbootinit.service.PictureService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 图片服务实现
 *
 */
@Service
public class PictureServiceImpl implements PictureService {


    /**
     * 从 Bing 抓取图片
     * @param pictureQueryRequest
     * @return
     */
    @Override
    public Page<PictureVO> grabPictureFromBing(PictureQueryRequest pictureQueryRequest) {
        String searchText = pictureQueryRequest.getSearchText();
        int current = pictureQueryRequest.getCurrent();
        int pageSize = pictureQueryRequest.getPageSize();
        String url = "https://cn.bing.com/images/search?q=" + searchText + "&first=" + current;
        Document doc = null;
        ArrayList<PictureVO> pictureVOS = new ArrayList<>();
        b:while(true){
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取图片失败！");
            }
            Elements elements = doc.select(".iuscp.isv");
            a:for (Element element : elements) {
                String imgInfo = element.select(".iusc").get(0).attr("m");
                Map map = JSONUtil.toBean(imgInfo, Map.class);
                String pictureUrl = map.get("murl").toString();
                String pictureTitle = map.get("t").toString();
                PictureVO pictureVO = new PictureVO();
                pictureVO.setUrl(pictureUrl);
                pictureVO.setTitle(pictureTitle);
                pictureVOS.add(pictureVO);
                if (pictureVOS.size() >= pageSize) {
                    break;
                }
            }
            if(pictureVOS.size() > 0){
                break;
            }
        }
        Page<PictureVO> pictureVOPage = new Page<>(current, pageSize, 9999);
        pictureVOPage.setRecords(pictureVOS);
        return pictureVOPage;
    }
}




