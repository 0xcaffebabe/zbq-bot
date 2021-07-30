package wang.ismy.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import wang.ismy.dto.EmoticonsDTO;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * 表情包服务
 */
public class EmoticonsService {

    private static final EmoticonsService EMOTICONS_SERVICE = new EmoticonsService();

    public byte[] selectRandomOne(String text) throws IOException {
        String response = HttpUtil.get("https://www.weshineapp.com/api/v1/index/search/" + URLEncoder.encode(text, "utf8") + "?offset=0&limit=18&block=list");
        JSONObject json = JSON.parseObject(response);
        if (json == null) {
            return new byte[]{};
        }
        JSONArray emoticonsList = json.getJSONArray("data");
        List<EmoticonsDTO> emoticonsDTOList = emoticonsList.toJavaList(EmoticonsDTO.class);
        if (CollectionUtil.isEmpty(emoticonsDTOList)) {
            return new byte[]{};
        }
        return HttpUtil.downloadBytes(emoticonsDTOList.get(RandomUtil.randomInt(0, emoticonsDTOList.size())).getUrl());
    }

    public static EmoticonsService getInstance() {
        return EMOTICONS_SERVICE;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(Arrays.toString(EMOTICONS_SERVICE.selectRandomOne("冒泡")));
    }
}
