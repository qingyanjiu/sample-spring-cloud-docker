package louis.demo.gateway.es;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Configuration
@EnableScheduling
@RestController
@RequestMapping("es")
public class ElasticSearchController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment environment;

    @RequestMapping("createIndex")
    public Object createIndex(@RequestParam(value="date",required = false) String specificDate){
        Object result = null;
        String host = environment.getProperty("es.host");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = "";
        //如果指定创建哪天的index，直接使用
        if(specificDate != null) {
            try {
                formatter.parse(specificDate);
                date = specificDate;
            } catch (ParseException e) {
                return "Date format error! Input date should be like 2019-01-01";
            }
        }
        //没指定，默认生成明天的index
        else {
            Date d = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            date = formatter.format(calendar.getTime());
        }
        String indexName = "zipkin-span-"+date;
        String url = host + "/" +indexName;
        HttpPut httpPut = new HttpPut(url);
        String json = "{" +
                "\"mappings\":{"+
                "\"_source\":{"+
                "\"excludes\":["+
                "\"_q\""+
                "]"+
                "},"+
                "\"dynamic_templates\":["+
                "{"+
                "\"strings\":{"+
                "\"match\":\"*\","+
                "\"match_mapping_type\":\"string\","+
                "\"mapping\":{"+
                "\"ignore_above\":256,"+
                "\"norms\":false,"+
                "\"type\":\"keyword\""+
                "}"+
                "}"+
                "}"+
                "],"+
                "\"properties\":{"+
                "\"_q\":{"+
                "\"type\":\"keyword\""+
                "},"+
                "\"annotations\":{"+
                "\"type\":\"object\","+
                "\"enabled\":false"+
                "},"+
                "\"duration\":{"+
                "\"type\":\"long\""+
                "},"+
                "\"id\":{"+
                "\"type\":\"keyword\","+
                "\"ignore_above\":256"+
                "},"+
                "\"kind\":{"+
                "\"type\":\"keyword\","+
                "\"ignore_above\":256"+
                "},"+
                "\"localEndpoint\":{"+
                "\"dynamic\":\"false\","+
                "\"properties\":{"+
                "\"serviceName\":{"+
                "\"type\":\"keyword\""+
                "}"+
                "}"+
                "},"+
                "\"name\":{"+
                "\"type\":\"keyword\""+
                "},"+
                "\"parentId\":{"+
                "\"type\":\"keyword\","+
                "\"ignore_above\":256"+
                "},"+
                "\"remoteEndpoint\":{"+
                "\"dynamic\":\"false\","+
                "\"properties\":{"+
                "\"serviceName\":{"+
                "\"type\":\"keyword\""+
                "}"+
                "}"+
                "},"+
                "\"tags\":{"+
                "\"type\":\"object\","+
                "\"enabled\":true"+
                "},"+
                "\"timestamp\":{"+
                "\"type\":\"long\""+
                "},"+
                "\"timestamp_millis\":{"+
                "\"type\":\"date\","+
                "\"format\":\"epoch_millis\""+
                "},"+
                "\"traceId\":{"+
                "\"type\":\"keyword\""+
                "}"+
                "}"+
                "}"+
                "}";
        // 构建消息实体
        StringEntity entity = new StringEntity(json, Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        // 发送Json格式的数据请求
        entity.setContentType("application/json");
        httpPut.setEntity(entity);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPut);
            int code = httpResponse.getStatusLine().getStatusCode();
            if(code == 200)
                logger.info("created index "+indexName+" successfully");
            else
                logger.error("create index "+indexName+" failed");

            BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            StringBuilder sb = new StringBuilder("");
            String line = "";
            String ls = System.getProperty("line.separator");
            if((line = br.readLine()) != null)
                sb.append(line + ls);
            br.close();
            result = JSON.toJSON(sb.toString());
        } catch (ClientProtocolException e) {
            System.out.println("create index failed");
        } catch (IOException e) {
            System.out.println("create index failed");
        }
        return result;
    }

    @Scheduled(cron = "0 0 12/3 * * *")
    void createTomorrowIndex(){
        this.createIndex(null);
    }
}
