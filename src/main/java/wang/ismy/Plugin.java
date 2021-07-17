package wang.ismy;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;
import wang.ismy.listener.impl.*;
import wang.ismy.service.SpinPenKeywordsRewardManager;
import wang.ismy.service.SpinPenSearchService;
import wang.ismy.service.SpinPenTrickVoiceService;
import wang.ismy.service.VideoSearchService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, new TrickVoiceSearchListener());
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, new RobotHelpListener());
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, new PenSellerMsgListener());
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, new AllGroupMessageListener());
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, new SongOrderMessageListener());

        new SpinPenKeywordsRewardManager().init(GlobalEventChannel.INSTANCE);

        GlobalEventChannel.INSTANCE.subscribeAlways(MemberJoinEvent.class, event -> {
            long id = event.getMember().getId();
            MessageChain msg = new At(id)
                    .plus("欢迎加入" + event.getGroup().getName())
                    .plus(", 我是转笔机器人");
            event.getGroup().sendMessage(msg);
        });

        GlobalEventChannel.INSTANCE.subscribeAlways(MemberJoinRequestEvent.class, event -> {
            String msg = event.getMessage();
            if (msg.contains("新人") || msg.contains("萌新")) {
                event.reject(false, "请回答一个转笔名词");
            }
        });
    }
}