package com.yupi.springbootinit.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

/**
 * 抓取文章数据
 *
 */
// todo 取消注释开启任务
//@Component
@Slf4j
public class GrabData implements CommandLineRunner {

    @Resource
    private PostService postService;


    @Override
    public void run(String... args) {
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
        if (b) {
            log.info("GrabData 任务执行成功，抓取文章数据成功！");
        }else {
            log.error("GrabData 任务执行失败，抓取文章数据失败！");
        }
    }
}
