package wang.ismy.listener.impl;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageSource;
import wang.ismy.dto.MessageBO;
import wang.ismy.listener.BaseGroupMessageListener;
import wang.ismy.service.MessageQueue;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @Title: PenSellerMsgListener
 * @description:
 * @author: cjiping@linewell.com
 * @since: 2021年06月08日 9:03
 */
public class PenSellerMsgListener extends BaseGroupMessageListener {

    private static final Pattern PATTERN =Pattern.compile("包邮|低价|材料|清笔|价格|问价|私聊|便宜");

    public PenSellerMsgListener() {
        super("");
    }

    @Override
    protected void consume(GroupMessageEvent event) {
        boolean currentContainsImg = event.getMessage()
                .stream()
                .anyMatch(Image.class::isInstance);
        long groupId = event.getGroup().getId();
        long qq = event.getSender().getId();
        boolean recentContainsImg = MessageQueue.getInstance()
                .getMessageSequence(groupId, qq)
                .stream().anyMatch(MessageBO::isImg);
        if (PATTERN.matcher(textMessage).find() &&(currentContainsImg || recentContainsImg)) {
            MessageSource.recall(event.getMessage());
        }
    }
}
