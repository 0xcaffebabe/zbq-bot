package wang.ismy;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpUtil;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

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
            File source = File.createTempFile("tts",".mp3");//输入
            FileOutputStream fos = new FileOutputStream(source);
            fos.write(bytes);
            fos.flush();
            fos.close();

            File target = File.createTempFile("tts", ".amr");//输出
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libamr_nb");//编码器

            audio.setBitRate(12200);//比特率
            audio.setChannels(1);//声道；1单声道，2立体声
            audio.setSamplingRate(8000);//采样率（重要！！！）

            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("amr");//格式
            attrs.setAudioAttributes(audio);//音频设置
            Encoder encoder = new Encoder();
            encoder.encode(source, target, attrs);

            return IoUtil.readBytes(new FileInputStream(target));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
