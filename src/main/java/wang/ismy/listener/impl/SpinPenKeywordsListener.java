package wang.ismy.listener.impl;

import cn.hutool.core.util.RandomUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import wang.ismy.dto.SpinPenKw;
import wang.ismy.listener.BaseGroupMessageListener;
import wang.ismy.service.DataAccessManager;
import wang.ismy.service.QQScoreMappingService;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @author MY
 * @date 2021/6/14 20:01
 */
public class SpinPenKeywordsListener extends BaseGroupMessageListener {

    private static Pattern PATTERN = Pattern.compile("转笔|VGG|IVAN|PEEM|连招|探讨|TRICK");
    private static final QQScoreMappingService QQ_SCORE_MAPPING_SERVICE = QQScoreMappingService.getInstance();

    {
        getRegexString();
    }

    public SpinPenKeywordsListener() {
        super("");
    }

    @Override
    protected void consume(GroupMessageEvent event) {
        String msg = textMessage.replaceAll(" ", "")
                .toUpperCase(Locale.ROOT);
        long qq = event.getSender().getId();
        if (PATTERN.matcher(msg).find()) {
            if (RandomUtil.randomInt(0, 7) == 3) {
                int score = QQ_SCORE_MAPPING_SERVICE.increaseRandom(qq);
                event.getSubject()
                        .sendMessage(new At(qq)
                                .plus("由于您表现良好， 本次奖励您 " + score + "积分, 您当前拥有 " + QQ_SCORE_MAPPING_SERVICE.get(qq) + " 积分（积分系统即将上线）"));
            }
        }
    }

    private static void getRegexString(){
        SpinPenKw spinPenKw = new DataAccessManager().read(SpinPenKw.class.getName(), SpinPenKw.class);
        if (spinPenKw == null) {
            spinPenKw = new SpinPenKw();
            new DataAccessManager().save(SpinPenKw.class.getName(), spinPenKw);
        }
        PATTERN = Pattern.compile(spinPenKw.getKw());
    }
}
