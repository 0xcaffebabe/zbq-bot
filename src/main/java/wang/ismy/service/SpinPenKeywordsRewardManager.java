package wang.ismy.service;

import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import wang.ismy.listener.impl.SpinPenKeywordsListener;

/**
 * @author MY
 * @date 2021/6/14 19:13
 */
public class SpinPenKeywordsRewardManager {

    public void init(GlobalEventChannel channel){
        channel.subscribeAlways(GroupMessageEvent.class, new SpinPenKeywordsListener());
    }
}
