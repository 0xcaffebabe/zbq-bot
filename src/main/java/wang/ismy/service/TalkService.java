package wang.ismy.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Title: TalkService
 * @description:
 * @author: cjiping@linewell.com
 * @since: 2021年08月05日 12:23
 */
public class TalkService {
    private static final TalkService instance = new TalkService();

    public String talk(String msg) {
        try {
            String response = HttpUtil.get("http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + URLEncoder.encode(msg, "utf8"));
            String content = (String) JSONPath.read(response, "$content");
            if (StringUtils.isNotEmpty(content)) {
                return content.replaceAll("\\{br}", "\n");
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public static TalkService getInstance() {
        return instance;
    }
}
