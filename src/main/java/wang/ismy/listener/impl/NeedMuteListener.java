package wang.ismy.listener.impl;

import cn.hutool.crypto.digest.mac.MacEngine;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import wang.ismy.listener.BaseGroupMessageListener;

/**
 * @author MY
 * @date 2021/5/27 21:26
 */
public class NeedMuteListener extends BaseGroupMessageListener {
    @Override
    protected void consume(GroupMessageEvent event) {
        long senderId = event.getSender().getId();
        if (textMessage.contains("我要禁言")) {
            event.getGroup().get(senderId).mute(10);
        }
    }
}
