package wang.ismy.job;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import wang.ismy.dto.DouYinVideoItem;
import wang.ismy.service.DouYinVideoSearchService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AutoSendDouYinMessageInAfternoon implements Job {
    private static final DouYinVideoSearchService DOU_YIN_VIDEO_SEARCH_SERVICE = DouYinVideoSearchService.getInstance();
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<DouYinVideoItem> videoList = null;
        try {
            videoList = DOU_YIN_VIDEO_SEARCH_SERVICE.searchVideo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<DouYinVideoItem> finalVideoList = videoList;
        Bot.getInstances()
                .stream()
                .flatMap(bot -> bot.getGroups().stream())
                .forEach(group -> {
                    Collections.shuffle(finalVideoList);
                    List<DouYinVideoItem> collect = finalVideoList
                            .stream()
                            .limit(3)
                            .collect(Collectors.toList());
                    MessageChainBuilder builder = new MessageChainBuilder();
                    for (DouYinVideoItem video : collect) {
                        builder.append(video.getTitle() + ":" + video.getLink() + "\n");
                    }
                    group.sendMessage("下午好 三点钟了 饮茶先啦");
                    group.sendMessage(builder.build());
                });
    }
}
