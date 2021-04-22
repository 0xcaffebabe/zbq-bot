package wang.ismy;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class VideoSearchServiceTest {

    @Test
    public void test() throws IOException {
        VideoSearchService videoSearchService = new VideoSearchService();
        JsonObject json = videoSearchService.getVideoList("转笔探讨");
        System.out.println(videoSearchService.convertTop3ToBotResp(json));
    }

    public static void main(String[] args) throws IOException {
        new VideoSearchServiceTest().test();
    }
}