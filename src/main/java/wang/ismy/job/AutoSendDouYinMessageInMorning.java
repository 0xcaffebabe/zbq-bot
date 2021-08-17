package wang.ismy.job;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import wang.ismy.dto.DouYinVideoItem;
import wang.ismy.service.DouYinVideoSearchService;
import wang.ismy.service.EmoticonsService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AutoSendDouYinMessageInMorning implements Job {
    private static final DouYinVideoSearchService DOU_YIN_VIDEO_SEARCH_SERVICE = DouYinVideoSearchService.getInstance();
    public static final EmoticonsService EMOTICONS_SERVICE = EmoticonsService.getInstance();
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
                    try {
                        Image image = group.uploadImage(ExternalResource.create(EMOTICONS_SERVICE.selectRandomOne("起床")));
                        group.sendMessage(image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    group.sendMessage(builder.build());
                });
    }
}
