package wang.ismy.listener.impl;

import cn.hutool.core.io.IoUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.utils.ExternalResource;
import wang.ismy.listener.BaseGroupMessageListener;

import java.io.InputStream;

/**
 * @author MY
 * @date 2021/5/26 21:54
 */
public class RobotHelpListener extends BaseGroupMessageListener {
    public RobotHelpListener() {
        super("");
    }

    @Override
    protected void consume(GroupMessageEvent event) {
        if (isAtMe) {
            if (textMessage.contains("菜单")) {
                event.getSubject().sendMessage("转笔机器人功能：\n" +
                        "视频搜索：发送视频搜索加关键词搜索转笔视频。\n" +
                        "点歌：发送点歌加关键词点歌。\n" +
                        "刷抖音：发送刷抖音刷抖音。\n" +
                        "招式名称：发送招式名称加术语搜索术语读音。");
            }
        }
    }
}
