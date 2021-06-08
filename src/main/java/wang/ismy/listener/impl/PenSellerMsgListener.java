package wang.ismy.listener.impl;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageSource;
import wang.ismy.listener.BaseGroupMessageListener;

/**
 * @Title: PenSellerMsgListener
 * @description:
 * @author: cjiping@linewell.com
 * @since: 2021年06月08日 9:03
 */
public class PenSellerMsgListener extends BaseGroupMessageListener {
    @Override
    protected void consume(GroupMessageEvent event) {
        boolean isSeller = false;
        boolean containsImg = event.getMessage().stream().anyMatch(Image.class::isInstance);
        if (textMessage.contains("包邮")) {
            isSeller = true;
        }
        if (textMessage.contains("出") && containsImg) {
            isSeller = true;
        }
        if (textMessage.contains("低价") && containsImg) {
            isSeller = true;
        }
        if (textMessage.contains("材料") && containsImg) {
            isSeller = true;
        }
        if (textMessage.contains("清笔") && containsImg) {
            isSeller = true;
        }
        if (isSeller) {
            MessageSource.recall(event.getMessage());
        }
    }
}
