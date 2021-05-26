package wang.ismy.listener;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;

import java.util.function.Consumer;

/**
 * @author MY
 * @date 2021/5/26 21:35
 */
public abstract class BaseGroupMessageListener implements Consumer<GroupMessageEvent> {

    protected String textMessage;
    protected boolean isAtMe;

    @Override
    public void accept(GroupMessageEvent event) {
        textMessage = event
                .getMessage()
                .stream()
                .filter(PlainText.class::isInstance)
                .findFirst().map(Message::contentToString)
                .orElse("");
        isAtMe = event
                .getMessage()
                .stream()
                .anyMatch(msg -> msg instanceof At && Bot.getInstanceOrNull(((At)msg).getTarget()) != null);
        consume(event);
    }

    /**
     * 接收到消息后，该方法会被调用
     * @param event 事件对象
     */
    protected abstract void consume(GroupMessageEvent event);
}
