package wang.ismy.listener;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;
import cn.hutool.http.HttpUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;
import wang.ismy.service.ImgOcrService;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author MY
 * @date 2021/5/26 21:35
 */
public abstract class BaseGroupMessageListener implements Consumer<GroupMessageEvent> {

    private static final ImgOcrService IMG_OCR_SERVICE = ImgOcrService.getInstance();

    private static final LRUCache<String, byte[]> IMG_BYTES_CACHE = CacheUtil.newLRUCache(1024);
    private static final LRUCache<byte[], String> OCR_CACHE = CacheUtil.newLRUCache(1024);

    protected String textMessage;
    protected boolean isAtMe;
    protected String keyword;

    public BaseGroupMessageListener(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void accept(GroupMessageEvent event) {
        textMessage = event
                .getMessage()
                .stream()
                .filter(PlainText.class::isInstance)
                .findFirst().map(Message::contentToString)
                .map(str -> str.replaceAll("\n", ""))
                .orElse("");
        String ocrText = imgOcr(event);
        textMessage += ocrText;
        if (!textMessage.contains(keyword)) {
            return;
        }
        isAtMe = event
                .getMessage()
                .stream()
                .anyMatch(msg -> msg instanceof At && Bot.getInstanceOrNull(((At)msg).getTarget()) != null);
        consume(event);
    }

    /**
     * 接收到消息后，该方法会被调用
     * @param event 事件对象
     */
    protected abstract void consume(GroupMessageEvent event);

    private List<byte[]> getImgListFromMessage(GroupMessageEvent event) {
        return event.getMessage()
                .stream()
                .filter(Image.class::isInstance)
                .map(Image.class::cast)
                .distinct()
                .map(img -> Mirai.getInstance().queryImageUrl(Bot.getInstances().stream().findAny().orElse(null), img))
                .map(url -> {
                    if (IMG_BYTES_CACHE.containsKey(url)) {
                        return IMG_BYTES_CACHE.get(url);
                    }
                    byte[] bytes = HttpUtil.downloadBytes(url);
                    IMG_BYTES_CACHE.put(url, bytes);
                    return bytes;
                })
                .distinct()
                .collect(Collectors.toList());
    }

    private String imgOcr(GroupMessageEvent event) {
        try {
            return getImgListFromMessage(event)
                    .stream()
                    .map(bytes -> {
                        if (OCR_CACHE.containsKey(bytes)) {
                            return OCR_CACHE.get(bytes);
                        }
                        String content = doOcr(bytes);
                        OCR_CACHE.put(bytes, content);
                        return content;
                    })
                    .collect(Collectors.joining("@@"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private String doOcr(byte[] bytes){
        try {
            return IMG_OCR_SERVICE.doOcr(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
