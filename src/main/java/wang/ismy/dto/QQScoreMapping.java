package wang.ismy.dto;

import cn.hutool.core.util.RandomUtil;
import wang.ismy.service.DataAccessManager;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author MY
 * @date 2021/6/14 19:47
 */
public class QQScoreMapping {

    private Map<Long, AtomicInteger> scoreMapping = new ConcurrentHashMap<>();

    public Map<Long, AtomicInteger> getScoreMapping() {
        return scoreMapping;
    }

    public void setScoreMapping(Map<Long, AtomicInteger> scoreMapping) {
        this.scoreMapping = scoreMapping;
    }
}
