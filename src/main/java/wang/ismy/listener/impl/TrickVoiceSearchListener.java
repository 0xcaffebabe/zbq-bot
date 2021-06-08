package wang.ismy.listener.impl;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;
import wang.ismy.service.SpinPenTrickVoiceService;
import wang.ismy.listener.BaseGroupMessageListener;

/**
 * @author MY
 * @date 2021/5/26 21:51
 */
public class TrickVoiceSearchListener extends BaseGroupMessageListener {
    private static final SpinPenTrickVoiceService SPIN_PEN_TRICK_VOICE_SERVICE = new SpinPenTrickVoiceService();
    @Override
    protected void consume(GroupMessageEvent event) {
        if (textMessage.contains("招式名称")) {
            event.getSubject().sendMessage("转码中,请稍候...");
            try {
                byte[] bytes = SPIN_PEN_TRICK_VOICE_SERVICE.speakTrick(textMessage.replaceAll("招式名称", ""));
                if (bytes == null) {
                    event.getSubject().sendMessage("招式名称读音搜索失败");
                    return;
                }
                Voice voice = event.getGroup().uploadVoice(ExternalResource.create(bytes));
                event.getSubject().sendMessage(voice);
            }catch (Exception e){
                event.getSubject().sendMessage("招式搜索失败 :" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
