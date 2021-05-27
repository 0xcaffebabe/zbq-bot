package wang.ismy;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;

import java.util.Map;

/**
 * @author MY
 * @date 2021/5/27 21:44
 */
public class MessageService {
    private static final LRUCache<String, String> messageCache = CacheUtil.newLRUCache(4096);
    private static final MessageService INSTANCE = new MessageService();

    public void add(String id, String msg) {
        messageCache.put(id, msg);
    }

    public String get(String id) {
        return messageCache.get(id);
    }

    public static MessageService getInstance() {
        return INSTANCE;
    }
}
