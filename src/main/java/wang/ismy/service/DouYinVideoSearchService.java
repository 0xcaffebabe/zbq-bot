package wang.ismy.service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import wang.ismy.dto.DouYinVideoItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DouYinVideoSearchService {

    private final WebClient webClient = new WebClient(BrowserVersion.CHROME);

    {
        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX

    }

    public List<DouYinVideoItem> searchVideo() throws IOException {
        HtmlPage page = webClient.getPage("https://www.douyin.com/search/%E8%BD%AC%E7%AC%94?publish_time=0&sort_type=0&source=normal_search&type=video");
        webClient.waitForBackgroundJavaScript(20000);
        String html = page.asXml();
        Document doc = Jsoup.parse(html);
        Elements videoUl = doc.select("#root > div > div:nth-child(2) > div > div > div:nth-child(2) > ul");
        Elements videoList = videoUl.select("li");
        List<DouYinVideoItem> douYinVideoList = new ArrayList<>();
        for (Element videoItem : videoList) {
            String title = videoItem.select("div > a > p > span > span > span > span > span").get(0).text();
            String img = videoItem.select("img").get(0).attr("src");
            String link = videoItem.select("div > a").get(0).attr("href");
            DouYinVideoItem douYinVideoItem = new DouYinVideoItem();
            douYinVideoItem.setTitle(title);
            douYinVideoItem.setImg(img);
            douYinVideoItem.setLink(link);
            douYinVideoList.add(douYinVideoItem);
        }
        return douYinVideoList;
    }

    public static void main(String[] args) throws IOException {
        List<DouYinVideoItem> douYinVideoItems = new DouYinVideoSearchService().searchVideo();
        System.out.println(douYinVideoItems);
    }
}
