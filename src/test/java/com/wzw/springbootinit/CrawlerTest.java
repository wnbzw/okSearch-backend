package com.wzw.springbootinit;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wzw.springbootinit.model.entity.Picture;
import com.wzw.springbootinit.model.entity.Post;
import com.wzw.springbootinit.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class CrawlerTest {
    @Resource
    private PostService postService;

    @Test
    void testFetchPicture() throws IOException {
        int current = 1;
        String url = "https://cn.bing.com/images/search?q=小黑子&first=" + current;
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictures = new ArrayList<>();
        for (Element element : elements) {
            // 取图片地址（murl）
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
//            System.out.println(murl);
            // 取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
//            System.out.println(title);
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(murl);
            pictures.add(picture);
        }
        System.out.println(pictures);
    }

    @Test
    public void test() {
        //1. 获取所有帖子
        String json="{\"pageSize\":12,\"sortOrder\":\"descend\",\"sortField\":\"createTime\",\"tags\":[],\"current\":2,\"reviewStatus\":1,\"category\":\"文章\",\"hiddenContent\":true}";
        String url="https://api.codefather.cn/api/post/list/page/vo";
        String result = HttpRequest.post(url).body(json).execute().body();
        // 2. 解析数据
        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        JSONObject  data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        List<Post> postList = new ArrayList<>();
        for (Object record : records) {
            JSONObject jsonObject = (JSONObject) record;
            Post post = new Post();
            post.setTitle(jsonObject.getStr("title"));
            post.setContent(jsonObject.getStr("content"));
            JSONArray tags = (JSONArray) jsonObject.get("tags");
            List<String> list = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(list));
            post.setUserId(1L);
            postList.add(post);
        }
        boolean b = postService.saveBatch(postList);
        Assertions.assertTrue(b);
    }
}
