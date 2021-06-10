package wang.ismy.listener.impl;

import com.google.common.util.concurrent.RateLimiter;
import it.sauronsoftware.jave.EncoderException;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;
import wang.ismy.listener.BaseGroupMessageListener;
import wang.ismy.service.MusicService;
import wang.ismy.util.ImgSendUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author MY
 * @date 2021/6/9 22:05
 */
public class SongOrderMessageListener extends BaseGroupMessageListener {
    private static final MusicService MUSIC_SERVICE = new MusicService();
    private static RateLimiter rateLimiter = RateLimiter.create(0.05);
    @Override
    protected void consume(GroupMessageEvent event) {
        if (textMessage.contains("点歌")) {
            if (!rateLimiter.tryAcquire(100, TimeUnit.MILLISECONDS)) {
                event.getSender().sendMessage("已触发限流 请稍后再试");
                return;
            }
            String songName = textMessage.replaceAll("点歌", "");
            event.getSubject().sendMessage("转码中 请稍后");
            try {
                Voice voice = event.getSubject().uploadVoice(ExternalResource.create(MUSIC_SERVICE.search(songName)));
                event.getSubject().sendMessage(voice);
                ImgSendUtils.send("call.gif", event.getSubject());
            } catch (IOException | EncoderException e) {
                event.getSubject().sendMessage("点歌失败：" + e.getMessage());
            }
        }
    }
}
