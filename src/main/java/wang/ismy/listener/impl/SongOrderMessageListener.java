package wang.ismy.listener.impl;

import com.google.common.util.concurrent.RateLimiter;
import it.sauronsoftware.jave.EncoderException;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;
import wang.ismy.listener.BaseGroupMessageListener;
import wang.ismy.listener.RateLimitedMessageListener;
import wang.ismy.service.MusicService;
import wang.ismy.util.ImgSendUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author MY
 * @date 2021/6/9 22:05
 */
public class SongOrderMessageListener extends RateLimitedMessageListener {
    private static final MusicService MUSIC_SERVICE = new MusicService();

    public SongOrderMessageListener() {
        super("点歌", "点歌");
    }

    @Override
    protected void consume(GroupMessageEvent event) {
        String songName = textMessage.replaceAll("点歌", "");
        try {
            Voice voice = event.getSubject().uploadVoice(ExternalResource.create(MUSIC_SERVICE.search(songName)));
            event.getSubject().sendMessage(voice);
        } catch (Exception e) {
            event.getSubject().sendMessage("点歌失败：" + e.getMessage());
        }
    }
}
