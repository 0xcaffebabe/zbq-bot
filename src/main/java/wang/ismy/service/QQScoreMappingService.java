package wang.ismy.service;

import cn.hutool.core.util.RandomUtil;
import wang.ismy.dto.QQScoreMapping;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author MY
 * @date 2021/6/14 19:57
 */
public class QQScoreMappingService {

    private static final QQScoreMappingService INSTANCE = new QQScoreMappingService();
    private static final DataAccessManager DATA_ACCESS_MANAGER = new DataAccessManager();

    private QQScoreMapping mapping;


    private QQScoreMappingService() {
        mapping = new DataAccessManager().read(QQScoreMapping.class.getName(), QQScoreMapping.class);
        if (mapping == null) {
            mapping = new QQScoreMapping();
        }
    }

    public int increaseRandom(Long qq){
        int score = RandomUtil.randomInt(1, 10);
        mapping.getScoreMapping().computeIfAbsent(qq, key -> new AtomicInteger())
                .addAndGet(score);
        DATA_ACCESS_MANAGER.save(QQScoreMapping.class.getName(), mapping);
        return score;
    }

    public int get(Long qq) {
        return mapping.getScoreMapping().computeIfAbsent(qq, key -> new AtomicInteger())
                .get();
    }

    public static QQScoreMappingService getInstance() {
        return INSTANCE;
    }
}
