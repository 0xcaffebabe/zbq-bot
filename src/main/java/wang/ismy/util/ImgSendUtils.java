package wang.ismy.util;

import cn.hutool.core.io.IoUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.InputStream;

/**
 * @Title: ImgSendUtils
 * @description:
 * @author: cjiping@linewell.com
 * @since: 2021年06月10日 12:15
 */
public class ImgSendUtils {

    public static void send(String filename, Contact contact){
        InputStream is = ImgSendUtils.class.getClassLoader().getResourceAsStream(filename);
        if (is != null) {
            byte[] bytes = IoUtil.readBytes(is);
            contact.sendMessage(contact.uploadImage(ExternalResource.create(bytes)));
        }
    }
}
