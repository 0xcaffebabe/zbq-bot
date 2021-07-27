package wang.ismy.listener;

import com.google.common.util.concurrent.RateLimiter;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;

import java.util.concurrent.TimeUnit;

public abstract class RateLimitedMessageListener extends BaseGroupMessageListener{

    private final RateLimiter rateLimiter = RateLimiter.create(0.05);

    private String name;

    public RateLimitedMessageListener(String name, String keyword) {
        super(keyword);
        this.name = name;
    }

    @Override
    public void accept(GroupMessageEvent event) {
        textMessage = event
                .getMessage()
                .stream()
                .filter(PlainText.class::isInstance)
                .findFirst().map(Message::contentToString)
                .map(str -> str.replaceAll("\n", ""))
                .orElse("");
        if (!textMessage.contains(keyword)) {
            return;
        }
        isAtMe = event
                .getMessage()
                .stream()
                .anyMatch(msg -> msg instanceof At && Bot.getInstanceOrNull(((At)msg).getTarget()) != null);
        if (!rateLimiter.tryAcquire(100, TimeUnit.MILLISECONDS)) {
            event.getSender().sendMessage(name + "已限流 请稍后再试");
            return;
        }
        consume(event);
    }
}
