package wang.ismy.listener.impl;

import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import wang.ismy.service.VideoSearchService;
import wang.ismy.dto.VideoItem;
import wang.ismy.listener.BaseGroupMessageListener;

import java.util.List;

/**
 * @author MY
 * @date 2021/5/26 21:43
 */
public class VideoSearchListener extends BaseGroupMessageListener {

    private static final VideoSearchService VIDEO_SEARCH_SERVICE = new VideoSearchService();

    @Override
    protected void consume(GroupMessageEvent event) {
        if (textMessage.contains("视频搜索")) {
            try {
                event.getSubject().sendMessage("搜索中...");
                String kw = textMessage.replaceAll("视频搜索", "");
                List<VideoItem> videoList = VIDEO_SEARCH_SERVICE.getTop3(kw);
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
        }
    }
}
