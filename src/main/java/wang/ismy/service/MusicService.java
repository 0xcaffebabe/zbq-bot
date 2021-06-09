package wang.ismy.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONPath;
import it.sauronsoftware.jave.EncoderException;
import wang.ismy.util.AudioUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author MY
 * @date 2021/6/9 21:39
 */
public class MusicService {

    public byte[] search(String song) throws IOException, EncoderException {
        String json = HttpUtil.post(String.format("http://music.163.com/api/search/get/web?csrf_token=hlpretag=&hlposttag=&s=%s&type=1&offset=0&total=true&limit=10", URLEncoder.encode(song, "utf8")), "");
        String songId = JSONPath.read(json, "$result.songs[0].id").toString();
        byte[] bytes = HttpUtil.downloadBytes("http://music.163.com/song/media/outer/url?id=" + songId + ".mp3");
        return AudioUtils.mp32amr(bytes);
    }

    public static void main(String[] args) throws IOException, EncoderException {
        new MusicService().search("鸡你太美");
    }
}
