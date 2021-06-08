package wang.ismy.service;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;
import com.google.common.collect.EvictingQueue;
import org.jetbrains.annotations.NotNull;
import wang.ismy.dto.MessageBO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author MY
 * @date 2021/6/8 22:26
 */
public class MessageQueue {

    private static final MessageQueue INSTANCE = new MessageQueue();

    private LRUCache<String, Map<String, EvictingQueue<MessageBO>>> groupMessage = CacheUtil.newLRUCache(4096);

    public void put(long longGroupId, long longQQ, MessageBO msg){
        EvictingQueue<MessageBO> messageMap = getMemberMessageMap(longGroupId, longQQ);
        messageMap.offer(msg);
    }

    @NotNull
    private EvictingQueue<MessageBO> getMemberMessageMap(long longGroupId, long longQQ) {
        String groupId = longGroupId + "";
        String qq = longQQ + "";
        if (!groupMessage.containsKey(groupId)) {
            groupMessage.put(groupId, new ConcurrentHashMap<>());
        }
        Map<String, EvictingQueue<MessageBO>> groupMessageMap = groupMessage.get(groupId);
        EvictingQueue<MessageBO> messageMap = groupMessageMap.computeIfAbsent(qq, key -> EvictingQueue.create(7));
        return messageMap;
    }

    public MessageBO getLast(long longGroupId, long longQQ) {
        EvictingQueue<MessageBO> messageMap = getMemberMessageMap(longGroupId, longQQ);
        if (messageMap.size() == 0) {
            return null;
        }
        if (messageMap.size() == 1) {
            return messageMap.peek();
        }
        return messageMap
                .stream()
                .skip(messageMap.size() - 1)
                .findFirst()
                .orElse(null);
    }

    public List<MessageBO> getMessageSequence(long longGroupId, long longQQ) {
        return new ArrayList<>(getMemberMessageMap(longGroupId, longQQ));
    }

    public static MessageQueue getInstance() {
        return INSTANCE;
    }
}
