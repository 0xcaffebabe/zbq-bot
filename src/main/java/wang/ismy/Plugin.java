package wang.ismy;

import cn.hutool.core.collection.CollectionUtil;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;
import org.apache.commons.codec.EncoderException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public final class Plugin extends JavaPlugin {
    public static final Plugin INSTANCE = new Plugin();
    private static final VideoSearchService videoSearchService = new VideoSearchService();
    private static final SpinPenSearchService spinPenSearchService = new SpinPenSearchService();

    private Plugin() {
        super(new JvmPluginDescriptionBuilder("wang.ismy.plugin", "1.0-SNAPSHOT")
                .name("penspinning robot")
                .author("my")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("Plugin loaded!");
    }

    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        Listener listener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
            String message = event.getMessage().contentToString();
            if (message.contains("视频搜索")) {
                try {
                    event.getSubject().sendMessage(videoSearchService.search(message.replaceAll("视频搜索", "")));
                } catch (Exception e) {
                    event.getSubject().sendMessage("搜索失败：" + e.getMessage());
                }
            } else if (message.contains("机器人")) {
                event.getSubject().sendMessage("回归中...");
            } else if (message.contains("转笔搜索")) {
                event.getSubject().sendMessage("搜索中，请稍后...");
                try {
                    List<byte[]> imgList = spinPenSearchService.searchSpinPen(message.replaceAll("转笔搜索", ""));
                    for (byte[] bytes : imgList) {
                        event.getSubject().sendMessage(event.getGroup().uploadImage(ExternalResource.create(bytes)));
                    }
                } catch (Exception e) {
                    event.getSubject().sendMessage("搜索失败 :" + e.getMessage());
                    e.printStackTrace();
                }
            } else if (message.contains("鸡你太美")) {
                File source = new File("C:\\Users\\MY\\Desktop\\tts.mp3");//输入
                File target = new File("D:/target.amr");//输出
                AudioAttributes audio = new AudioAttributes();
                audio.setCodec("libamr_nb");//编码器

                audio.setBitRate(12200);//比特率
                audio.setChannels(1);//声道；1单声道，2立体声
                audio.setSamplingRate(8000);//采样率（重要！！！）

                EncodingAttributes attrs = new EncodingAttributes();
                attrs.setFormat("amr");//格式
                attrs.setAudioAttributes(audio);//音频设置
                Encoder encoder = new Encoder();
                try {
                    encoder.encode(source, target, attrs);
                } catch (InputFormatException e) {
                    e.printStackTrace();
                } catch (it.sauronsoftware.jave.EncoderException e) {
                    e.printStackTrace();
                }
                Voice voice = event.getGroup().uploadVoice(ExternalResource.create(new File("D:/target.amr")));
                event.getSubject().sendMessage(voice);
            }

        });
    }
}