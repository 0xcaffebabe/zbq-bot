package wang.ismy.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpUtil;
import it.sauronsoftware.jave.*;
import wang.ismy.util.AudioUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.URLEncoder;

/**
 * @author MY
 * @date 2021/5/23 22:21
 */
public class SpinPenTrickVoiceService {

    public byte[] speakTrick(String content){
        try {
            byte[] bytes = HttpUtil.downloadBytes("https://fanyi.baidu.com/gettts?lan=en&text=" + URLEncoder.encode(content, "utf8") + "&spd=3&source=web");
            return AudioUtils.mp32amr(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
