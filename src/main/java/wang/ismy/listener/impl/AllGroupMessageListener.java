package wang.ismy.listener.impl;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import wang.ismy.dto.MessageBO;
import wang.ismy.listener.BaseGroupMessageListener;
import wang.ismy.service.MessageQueue;

/**
 * @author MY
 * @date 2021/6/8 22:40
 */
public class AllGroupMessageListener extends BaseGroupMessageListener {
    @Override
    protected void consume(GroupMessageEvent event) {
        MessageBO messageBO = new MessageBO();
        messageBO.setMsg(textMessage);
        messageBO.setImg(event.getMessage().stream().anyMatch(Image.class::isInstance));

        long groupId = event.getGroup().getId();
        long qq = event.getSender().getId();


        System.out.println("last msg:" + MessageQueue.getInstance().getLast(groupId, qq));
        MessageQueue.getInstance().put(groupId, qq, messageBO);
    }
}
