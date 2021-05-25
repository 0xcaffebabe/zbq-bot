package wang.ismy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpUtil;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;
import kotlin.jvm.functions.Function1;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import org.apache.commons.codec.EncoderException;
import org.jetbrains.annotations.NotNull;
import wang.ismy.dto.VideoItem;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public final class Plugin extends JavaPlugin {
    public static final Plugin INSTANCE = new Plugin();
    private static final VideoSearchService videoSearchService = new VideoSearchService();
    private static final SpinPenSearchService spinPenSearchService = new SpinPenSearchService();
    private static final SpinPenTrickVoiceService spinPenTrickVoiceService = new SpinPenTrickVoiceService();

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
            String message = event
                    .getMessage()
                    .stream()
                    .filter(PlainText.class::isInstance)
                    .findFirst().map(Message::contentToString)
                    .orElse("");
            boolean isAtMe = event
                    .getMessage()
                    .stream()
                    .anyMatch(At.class::isInstance);
            if (message.contains("视频搜索")) {
                try {
                    event.getSubject().sendMessage("搜索中...");
                    List<VideoItem> videoList = videoSearchService.getTop3(message.replaceAll("视频搜索", ""));
                    MessageChainBuilder builder = new MessageChainBuilder();
                    for (VideoItem video : videoList) {
                        Image image = event.getSubject().uploadImage(ExternalResource.create(HttpUtil.downloadBytes("http:" + video.getThumbnail())));
                        builder.append(new PlainText(HtmlUtil.cleanHtmlTag(video.getTitle()) + "\n"))
                                .append(image)
                                .append("\n" + video.getLink() + "\n");
                    }
                    event.getSubject().sendMessage(builder.build());
                } catch (Exception e) {
                    e.printStackTrace();
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
            } else if (message.contains("招式名称")) {
                event.getSubject().sendMessage("转码中,请稍候...");
                try {
                    byte[] bytes = spinPenTrickVoiceService.speakTrick(message.replaceAll("招式名称", ""));
                    if (bytes == null) {
                        event.getSubject().sendMessage("招式名称读音搜索失败");
                        return;
                    }
                    Voice voice = event.getGroup().uploadVoice(ExternalResource.create(bytes));
                    event.getSubject().sendMessage(voice);
                }catch (Exception e){
                    event.getSubject().sendMessage("招式搜索失败 :" + e.getMessage());
                    e.printStackTrace();
                }
            }else if (isAtMe) {
                event.getSubject().sendMessage("转笔机器人功能：\n" +
                        "1. 转笔搜索：发送转笔搜索加笔名搜索转笔图片。\n" +
                        "2. 视频搜索：发送视频搜索加关键词搜索转笔视频。\n" +
                        "3. 招式名称：发送招式名称加术语搜索术语读音。");
            }

        });
    }
}