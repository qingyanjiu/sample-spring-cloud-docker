package louis.demo.sidecar;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.sidecar.EnableSidecar;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Properties;

@EnableSidecar
@SpringBootApplication
@Controller("/")
public class SidecarApplication {

    @Value("${spring.cloud.config.uri}")
    private String configServerUrl = "";

    @Value("${spring.cloud.config.profile}")
    private String configServerProfile = "";

    @Value("${spring.application.name}")
    private String appName = "";

    public static void main(String[] args) {
        SpringApplication.run(SidecarApplication.class, args);
    }


    @RequestMapping("properties")
    @ResponseBody
    public Properties getProperties() {
        StringBuffer url = new StringBuffer();
        url.append(configServerUrl)
                .append("/")
                .append(appName)
                .append("-")
                .append(configServerProfile)
                .append(".properties");
        HttpGet httpGet = new HttpGet(url.toString());
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse httpresponse = null;
        String response = "";
        Properties result = new Properties();
        try {
            httpresponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpresponse.getEntity();
            if (httpEntity != null) {
                response = EntityUtils.toString(httpEntity, "utf-8");
                String[] lines = response.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    String line = lines[i];
                    String key = line.substring(0, line.indexOf(":"));
                    String value = line.substring(line.indexOf(":") + 1, line.length());
                    result.put(key.trim(), StringEscapeUtils.unescapeJava(value).trim());
                }
            }
        } catch (ClientProtocolException e) {
            System.out.println("http request failed，uri: " + url.toString() + ",exception: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("http request failed，uri: " + url.toString() + ",exception: " + e.getMessage());
        }
        return result;
    }
}
