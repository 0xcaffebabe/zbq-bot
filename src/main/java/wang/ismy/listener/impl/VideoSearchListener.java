package wang.ismy.listener.impl;

import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpUtil;
import com.google.common.util.concurrent.RateLimiter;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import wang.ismy.listener.RateLimitedMessageListener;
import wang.ismy.service.VideoSearchService;
import wang.ismy.dto.VideoItem;
import wang.ismy.listener.BaseGroupMessageListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author MY
 * @date 2021/5/26 21:43
 */
public class VideoSearchListener extends RateLimitedMessageListener {

    private static final VideoSearchService VIDEO_SEARCH_SERVICE = new VideoSearchService();
    private static RateLimiter rateLimiter = RateLimiter.create(0.05);

    public VideoSearchListener() {
        super("视频搜索", "视频搜索");
    }

    @Override
    protected void consume(GroupMessageEvent event) {
        try {
            event.getSubject().sendMessage("搜索中...");
            String kw = textMessage.replaceAll("视频搜索", "");
            List<VideoItem> videoList = VIDEO_SEARCH_SERVICE.getTop3(kw);
            MessageChainBuilder builder = new MessageChainBuilder();
            for (VideoItem video : videoList) {
                try {
                    Image image = event.getSubject().uploadImage(ExternalResource.create(HttpUtil.downloadBytes("http:" + video.getThumbnail())));
                    builder.append(new PlainText(HtmlUtil.cleanHtmlTag(video.getTitle()) + "\n"))
                            .append(image)
                            .append("\n" + video.getLink() + "\n");
                } catch (Exception e) {
                    e.printStackTrace();
                    builder.append(new PlainText(HtmlUtil.cleanHtmlTag(video.getTitle()) + "\n"))
                            .append("\n" + video.getLink() + "\n");
                }
            }
            if (videoList.size() == 0) {
                event.getSubject().sendMessage("无法搜索 " + kw);
            } else {
                event.getSubject().sendMessage(builder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.getSubject().sendMessage("搜索失败：" + e.getMessage());
        }
    }
}
