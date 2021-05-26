package wang.ismy.listener.impl;

import cn.hutool.core.collection.CollectionUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import wang.ismy.SpinPenSearchService;
import wang.ismy.listener.BaseGroupMessageListener;

import java.util.List;

/**
 * @author MY
 * @date 2021/5/26 21:50
 */
public class SpinPenSearchListener extends BaseGroupMessageListener {
    private static final SpinPenSearchService SPIN_PEN_SEARCH_SERVICE = new SpinPenSearchService();
    @Override
    protected void consume(GroupMessageEvent event) {
        if (textMessage.contains("转笔搜索")) {
            event.getSubject().sendMessage("搜索中，请稍后...");
            try {
                List<byte[]> imgList = SPIN_PEN_SEARCH_SERVICE.searchSpinPen(textMessage.replaceAll("转笔搜索", ""));
                MessageChainBuilder builder = new MessageChainBuilder();
                for (byte[] bytes : imgList) {
                    builder.append(event.getGroup().uploadImage(ExternalResource.create(bytes)));
                }
                if (CollectionUtil.isEmpty(imgList)){
                    event.getSubject().sendMessage("无法搜索到相应转笔图片");
                    return;
                }
                event.getSubject().sendMessage(builder.build());
            } catch (Exception e) {
                event.getSubject().sendMessage("搜索失败 :" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
