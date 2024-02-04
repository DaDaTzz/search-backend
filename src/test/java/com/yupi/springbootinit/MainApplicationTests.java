package com.yupi.springbootinit;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.config.WxOpenConfig;
import javax.annotation.Resource;

import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 主类测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private WxOpenConfig wxOpenConfig;
    @Resource
    private PostService postService;

    @Test
    void contextLoads() {
        System.out.println(wxOpenConfig);
    }

    @Test
    void grabPost(){
        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String res = HttpRequest.post(url).body(json).execute().body();
        // System.out.println(res);
        Map map = JSONUtil.toBean(res, Map.class);
//        System.out.println(map);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
//        System.out.println(records);
        ArrayList<Post> posts = new ArrayList<>();
        for (Object record : records) {
            JSONObject temp = (JSONObject) record;
            String title = temp.getStr("title");
            String content = temp.getStr("content");
            JSONArray tags = (JSONArray) temp.get("tags");
            String jsonTags = JSONUtil.toJsonStr(tags);
            Post post = new Post();
            post.setTitle(title);
            post.setTags(jsonTags);
            post.setContent(content);
            post.setUserId(1L);
            posts.add(post);
        }
        boolean b = postService.saveBatch(posts);
        Assertions.assertTrue(b);
    }

    @Test
    void grabPicture() throws IOException {
        int current = 1;
        String url = "https://cn.bing.com/images/search?q=小黑子&first=" + current;
        Document doc = Jsoup.connect(url).get();
        Elements newsHeadlines = doc.select(".iuscp.isv");
        for (Element headline : newsHeadlines) {
            String imgInfo = headline.select(".iusc").get(0).attr("m");
            Map map = JSONUtil.toBean(imgInfo, Map.class);
            String imgUrl = map.get("murl").toString();
            String imgTitle = map.get("t").toString();
//            System.out.println(imgTitle);
//            System.out.println(imgUrl);
        }

    }
}
