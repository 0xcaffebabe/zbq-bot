package wang.ismy;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import wang.ismy.dto.VideoItem;
import wang.ismy.service.VideoSearchService;

import java.io.IOException;
import java.util.List;

class VideoSearchServiceTest {

    @Test
    public void test() throws IOException {
        VideoSearchService videoSearchService = new VideoSearchService();
        JsonObject json = videoSearchService.getVideoList("转笔探讨");
        System.out.println(videoSearchService.convertTop3ToBotResp(json));
    }

    @Test
    public void testGetTop3(){
        VideoSearchService videoSearchService = new VideoSearchService();
        List<VideoItem> videoList = videoSearchService.getTop3("转笔探讨");
        System.out.println(videoList);
    }

    public static void main(String[] args) throws IOException {
        new VideoSearchServiceTest().testGetTop3();
    }
}