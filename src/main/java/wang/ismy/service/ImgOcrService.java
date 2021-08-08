package wang.ismy.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONPatch;
import com.alibaba.fastjson.JSONPath;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jetty.http.MultiPartFormInputStream;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Base64;
import java.util.stream.Collectors;

public class ImgOcrService {
    private static final ImgOcrService instance = new ImgOcrService();

    public String doOcr(byte[] img) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://192.168.0.101:8089/api/tr-run/");
        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addTextBody("compress", "960")
                .addTextBody("img", Base64.getEncoder().encodeToString(img))
                .build();
        httpPost.setEntity(httpEntity);
        CloseableHttpResponse response = client.execute(httpPost);
        String result = IoUtil.read(response.getEntity().getContent(), "utf8");
        response.getEntity().getContent().close();
        JSONArray array = (JSONArray) JSONPath.read(result, "$data.raw_out");
        return array.stream()
                .map(JSONArray.class::cast)
                .map(arr -> arr.getString(1))
                .filter(StringUtils::hasLength)
                .map(str -> str.substring(2))
                .filter(StringUtils::hasLength)
                .collect(Collectors.joining());
    }

    public static ImgOcrService getInstance() {
        return instance;
    }

    public static void main(String[] args) throws IOException {
        byte[] bytes = IoUtil.readBytes(new FileInputStream("C:\\Users\\MY\\Pictures\\202185213811.jpg"), true);
        System.out.println(bytes.length);
        System.out.println(new ImgOcrService().doOcr(bytes));
    }
}
