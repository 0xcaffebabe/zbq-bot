package wang.ismy.listener.impl;

import cn.hutool.core.util.RandomUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.utils.ExternalResource;
import org.apache.commons.lang3.StringUtils;
import wang.ismy.listener.BaseGroupMessageListener;
import wang.ismy.service.EmoticonsService;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class RandomEmoticonsListener extends BaseGroupMessageListener {
    private static final EmoticonsService EMOTICONS_SERVICE = EmoticonsService.getInstance();
    public RandomEmoticonsListener() {
        super("");
    }

    @Override
    protected void consume(GroupMessageEvent event) {
        if (StringUtils.isNotBlank(textMessage) && textMessage.length() <= 5){
            if (RandomUtil.randomInt(0,3) == 1) {
                try {
                    event.getSubject().sendMessage(event.getSubject().uploadImage(ExternalResource.create(EMOTICONS_SERVICE.selectRandomOne(textMessage))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
