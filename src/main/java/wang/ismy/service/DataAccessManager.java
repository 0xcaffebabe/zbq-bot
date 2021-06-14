package wang.ismy.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author MY
 * @date 2021/6/14 19:36
 */
public class DataAccessManager {


    public void save(String key, Object data) {
        String json = JSONUtil.toJsonPrettyStr(data);
        try(FileOutputStream fos = new FileOutputStream(getName(key))){
            fos.write(json.getBytes(StandardCharsets.UTF_8));
            fos.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public <T> T read(String key, Class<T> clazz){
        try {
            byte[] bytes = IoUtil.readBytes(new FileInputStream(getName(key)), true);
            return JSONUtil.toBean(new String(bytes), clazz);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    private String getName(String key) {
        return "data/" + key + ".json";
    }
}
