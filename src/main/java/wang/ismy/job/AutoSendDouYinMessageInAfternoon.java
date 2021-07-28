package wang.ismy.job;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import wang.ismy.dto.DouYinVideoItem;
import wang.ismy.service.DouYinVideoSearchService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AutoSendDouYinMessageInAfternoon implements Job {
    private static final DouYinVideoSearchService DOU_YIN_VIDEO_SEARCH_SERVICE = DouYinVideoSearchService.getInstance();
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Bot.getInstances()
                .stream()
                .flatMap(bot -> bot.getGroups().stream())
                .forEach(group -> {
                    try {
                        List<DouYinVideoItem> videoList = DOU_YIN_VIDEO_SEARCH_SERVICE.searchVideo()
                                .stream()
                                .limit(3)
                                .collect(Collectors.toList());
                        MessageChainBuilder builder = new MessageChainBuilder();
                        for (DouYinVideoItem video : videoList) {
                            builder.append(video.getTitle() + ":" + video.getLink() + "\n");
                        }
                        group.sendMessage("下午好 三点钟了 饮茶先啦");
                        group.sendMessage(builder.build());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
