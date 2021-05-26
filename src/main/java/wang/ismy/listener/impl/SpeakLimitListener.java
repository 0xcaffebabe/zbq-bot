package wang.ismy.listener.impl;

import com.google.common.collect.EvictingQueue;
import com.google.common.util.concurrent.RateLimiter;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import wang.ismy.listener.BaseGroupMessageListener;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author MY
 * @date 2021/5/26 21:55
 */
public class SpeakLimitListener extends BaseGroupMessageListener {
    private static Map<Long, Map<Long, EvictingQueue<Long>>> valve = new ConcurrentHashMap<>();

    @Override
    protected void consume(GroupMessageEvent event) {
        long groupId = event.getGroup().getId();
        long senderId = event.getSender().getId();
        valve.computeIfAbsent(groupId, key -> new ConcurrentHashMap<>());
        valve.get(groupId).computeIfAbsent(senderId, key -> EvictingQueue.create(5));

        EvictingQueue<Long> queue = valve.get(groupId).get(senderId);
        long now = System.currentTimeMillis();
        queue.offer(now);
        if (queue.size() > 4 && now - queue.peek() < 10000) {
            event.getSubject().sendMessage(new At(senderId).plus("您已超速"));
        }
    }
}
