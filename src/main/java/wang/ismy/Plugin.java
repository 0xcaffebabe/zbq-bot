package wang.ismy;

import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.utils.ExternalResource;
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
            if (message.contains("视频搜索")){
                event.getSubject().sendMessage(videoSearchService.search(message.replaceAll("视频搜索", "")));
            }else if (message.contains("机器人")){
                event.getSubject().sendMessage("回归中...");
            }else if (message.contains("转笔搜索")){
                event.getSubject().sendMessage("搜索中，请稍后...");
                List<byte[]> imgList = spinPenSearchService.searchSpinPen(message.replaceAll("转笔搜索", ""));
                for (byte[] bytes : imgList) {
                    event.getSubject().sendMessage(event.getGroup().uploadImage(ExternalResource.create(bytes)));
                }
            }
        });
    }
}