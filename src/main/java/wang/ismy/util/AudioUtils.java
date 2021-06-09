package wang.ismy.util;

import cn.hutool.core.io.IoUtil;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author MY
 * @date 2021/6/9 21:40
 */
public class AudioUtils {

    public static byte[] mp32amr(byte[] bytes) throws IOException, EncoderException {
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
    }
}
