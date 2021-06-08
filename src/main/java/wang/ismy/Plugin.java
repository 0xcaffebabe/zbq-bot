package wang.ismy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpUtil;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;
import kotlin.jvm.functions.Function1;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.MessageRecallEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import org.apache.commons.codec.EncoderException;
import org.jetbrains.annotations.NotNull;
import wang.ismy.dto.VideoItem;
import wang.ismy.listener.impl.*;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
//        GlobalEventChannel.INSTANCE.subscribeAlways(BotOnlineEvent.class, event -> {
//            Bot.getInstances()
//                    .forEach(bot -> {
//                        bot.getGroups()
//                                .forEach(group -> {
//                                    group.sendMessage(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " 转笔机器人/司机终结者 已上线");
//                                });
//                    });
//        });
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, new VideoSearchListener());
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, new SpinPenSearchListener());
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, new TrickVoiceSearchListener());
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, new RobotHelpListener());
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, new PenSellerMsgListener());
    }
}