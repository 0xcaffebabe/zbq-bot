package wang.ismy.listener.impl;

import it.sauronsoftware.jave.EncoderException;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;
import wang.ismy.listener.BaseGroupMessageListener;
import wang.ismy.service.MusicService;

import java.io.IOException;

/**
 * @author MY
 * @date 2021/6/9 22:05
 */
public class SongOrderMessageListener extends BaseGroupMessageListener {
    private static final MusicService MUSIC_SERVICE = new MusicService();
    @Override
    protected void consume(GroupMessageEvent event) {
        if (textMessage.contains("点歌")) {
            String songName = textMessage.replaceAll("点歌", "");
            event.getSubject().sendMessage("转码中 请稍后");
            try {
                Voice voice = event.getSubject().uploadVoice(ExternalResource.create(MUSIC_SERVICE.search(songName)));
                event.getSubject().sendMessage(voice);
            } catch (IOException | EncoderException e) {
                event.getSubject().sendMessage("点歌失败：" + e.getMessage());
            }
        }
    }
}
