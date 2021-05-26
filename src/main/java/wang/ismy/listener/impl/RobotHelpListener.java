package wang.ismy.listener.impl;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import wang.ismy.listener.BaseGroupMessageListener;

/**
 * @author MY
 * @date 2021/5/26 21:54
 */
public class RobotHelpListener extends BaseGroupMessageListener {
    @Override
    protected void consume(GroupMessageEvent event) {
        if (textMessage.contains("机器人")) {
            event.getSubject().sendMessage("回归中...");
        }else if (isAtMe) {
            event.getSubject().sendMessage("转笔机器人功能：\n" +
                    "1. 转笔搜索：发送转笔搜索加笔名搜索转笔图片。\n" +
                    "2. 视频搜索：发送视频搜索加关键词搜索转笔视频。\n" +
                    "3. 招式名称：发送招式名称加术语搜索术语读音。");
        }
    }
}
