package wang.ismy.listener.impl;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import wang.ismy.listener.BaseGroupMessageListener;
import wang.ismy.service.TalkService;

/**
 * @Title: RandomTalkListener
 * @description: 随机闲聊
 * @author: cjiping@linewell.com
 * @since: 2021年08月05日 12:22
 */
public class RandomTalkListener extends BaseGroupMessageListener {
    private static final TalkService TALK_SERVICE = TalkService.getInstance();
    public RandomTalkListener() {
        super("");
    }

    @Override
    protected void consume(GroupMessageEvent event) {
        if (StringUtils.isNotEmpty(textMessage)) {
            if (RandomUtils.nextInt(0, 5) == 1) {
                String msg = TALK_SERVICE.talk(textMessage);
                if (StringUtils.isNotEmpty(msg)) {
                    event.getSubject().sendMessage(msg);
                }
                System.out.println(textMessage + "无言以对");
            }
        }
    }
}
