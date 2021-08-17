package wang.ismy.service;

import cn.hutool.http.HtmlUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import wang.ismy.dto.VideoItem;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: VideoSearchService
 * @description:
 * @author: cjiping@linewell.com
 * @since: 2021年04月22日 15:03
 */
public class VideoSearchService {
    private static final VideoSearchService INSTANCE = new VideoSearchService();

    private HttpClient httpClient;

    public void login() throws IOException {
        httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("https://zbq.ismy.wang/user/login");
        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setEntity(new StringEntity("{\n" +
                "    \"username\":\"test\",\n" +
                "    \"password\": \"202CB962AC59075B964B07152D234B70\"\n" +
                "}"));
        HttpResponse response = httpClient.execute(httpPost);
        String json = getResponse(response);
        if (json.contains("登录成功")){
            System.out.println("登录成功");
        }
    }

    public String getResponse(HttpResponse httpResponse) throws IOException {
        try {
            return IOUtils.toString(httpResponse.getEntity().getContent(), "utf8");
        }finally {
            httpResponse.getEntity().getContent().close();
        }
    }

    public JsonObject getVideoList(String kw) throws IOException {
        kw = "转笔 " + kw;
        if (httpClient == null){
            login();
        }
        HttpGet httpGet  = new HttpGet("https://zbq.ismy.wang/video/search?kw="+ URLEncoder.encode(kw, "utf8") +"&engine=0&page=1&length=20");
        HttpResponse response = httpClient.execute(httpGet);
        String json = getResponse(response);
        if (json.contains("未登录")){
            System.out.println("未登录 重新登录");
            login();
            return getVideoList(kw);
        }
        return new Gson().fromJson(json, JsonObject.class);
    }

    public String convertTop3ToBotResp(JsonObject jsonObject){
        JsonArray jsonArray = jsonObject.getAsJsonArray("data");
        int counter = 0;
        String resp  = "";
        for (JsonElement jsonElement : jsonArray) {
            if (counter >= 3) {
                break;
            }
            JsonElement title = jsonElement.getAsJsonObject().get("title");
            JsonElement link = jsonElement.getAsJsonObject().get("link");
            resp += HtmlUtil.cleanHtmlTag(title.toString()) + "--" + link.getAsString() + "\n";
            counter++;
        }
        return resp;
    }

    public String search(String kw){
        try {
            return convertTop3ToBotResp(getVideoList(kw));
        } catch (IOException e) {
            return "";
        }
    }

    public List<VideoItem> getTop3(String kw) {
        try {
            JsonObject videoList = getVideoList(kw);
            JsonArray dataList = videoList.getAsJsonArray("data");
            List<VideoItem> videoItemList = new Gson().fromJson(dataList.toString(), new TypeToken<List<VideoItem>>() {}.getType());
            return videoItemList
                    .stream().limit(3)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static VideoSearchService getInstance() {
        return INSTANCE;
    }
}
