package wang.ismy.service;


import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author MY
 * @date 2021/5/23 18:10
 */
public class SpinPenSearchService {

    public List<byte[]> searchSpinPen(String penName){
        try {
            String url = String.format("https://image.so.com/j?q=%s&src=&correct=&sn=1&pn=10", URLEncoder.encode("转笔" + penName, "utf8"));
            String response = HttpUtil.get(url);
            JSONArray jsonArray = (JSONArray) JSONPath.read(response, "$list");
            List<byte[]> result = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                if (i >= 3) {
                    break;
                }
                JSONObject json = jsonArray.getJSONObject(i);
                String imgUrl = json.getString("img");
                result.add(HttpUtil.downloadBytes(imgUrl));
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
