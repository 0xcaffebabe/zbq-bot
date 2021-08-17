package wang.ismy.job;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import wang.ismy.service.EmoticonsService;

import java.io.IOException;

/**
 * @Title: RobotHelpJob
 * @description:
 * @author: cjiping@linewell.com
 * @since: 2021年08月17日 15:54
 */
public class RobotHelpJob implements Job {

    public static final EmoticonsService EMOTICONS_SERVICE = EmoticonsService.getInstance();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Bot.getInstances()
                .stream()
                .flatMap(bot -> bot.getGroups().stream())
                .forEach(group -> {
                    try {
                        Image image = group.uploadImage(ExternalResource.create(EMOTICONS_SERVICE.selectRandomOne("狗")));
                        group.sendMessage(image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    group.sendMessage("转笔机器人功能：\n" +
                            "视频搜索：发送视频搜索加关键词搜索转笔视频。\n" +
                            "点歌：发送点歌加关键词点歌。\n" +
                            "刷抖音：发送刷抖音刷抖音。\n" +
                            "招式名称：发送招式名称加术语搜索术语读音。");
                });
    }
}
