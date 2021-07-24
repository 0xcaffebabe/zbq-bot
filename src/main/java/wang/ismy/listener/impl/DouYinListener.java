package wang.ismy.listener.impl;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import wang.ismy.dto.DouYinVideoItem;
import wang.ismy.listener.BaseGroupMessageListener;
import wang.ismy.service.DouYinVideoSearchService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DouYinListener extends BaseGroupMessageListener {
    private static final DouYinVideoSearchService DOU_YIN_VIDEO_SEARCH_SERVICE = new DouYinVideoSearchService();
    @Override
    protected void consume(GroupMessageEvent event) {
        if (event.getMessage().contentToString().contains("刷抖音")) {
            event.getSubject().sendMessage("正在获取相关转笔短视频");
            try {
                List<DouYinVideoItem> videoList = DOU_YIN_VIDEO_SEARCH_SERVICE.searchVideo()
                        .stream()
                        .limit(3)
                        .collect(Collectors.toList());
                MessageChainBuilder builder = new MessageChainBuilder();
                for (DouYinVideoItem video : videoList) {
                    builder.append(video.getTitle() + ":" + video.getLink() + "\n");
                }
                event.getSubject().sendMessage(builder.build());
            } catch (IOException e) {
                event.getSubject().sendMessage("获取失败 " + e.getMessage());
            }
        }
    }
}
