package com.cisco.webex.masterdatavalidation;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SidecarPropertyConfigurer extends PropertySourcesPlaceholderConfigurer {

    private static Logger logger = Logger.getLogger(SidecarPropertyConfigurer.class);

    private static final String CONFIG_URL = "http://localhost:30000/properties/";

    private HttpClientBuilder httpClientBuilder;

    private String profile = "dev";

    private static Map<String,String> profileMap;

    private Environment environment;

    static {
        profileMap = new HashMap();
        profileMap.put("STGING","dev");
        profileMap.put("PROD","prod");
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Properties mergeProperties()
    {
        String url = CONFIG_URL;
        String life = environment.getProperty("cisco.life");
        profile = profileMap.get(life);
        url += profile;
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpresponse = null;

        logger.info("prepare to get configuration from["+url+"]");

        if (httpClientBuilder == null) {
            // 创建HttpClientBuilder
            httpClientBuilder = HttpClientBuilder.create();
        }
        // 设置BasicAuth
        CredentialsProvider provider = new BasicCredentialsProvider();
        // Create the authentication scope
        AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
        // Create credential pair
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("test","test");
        // Inject the credentials
        provider.setCredentials(scope, credentials);
        // Set the default credentials provider
        httpClientBuilder.setDefaultCredentialsProvider(provider);
//        // Set the default cookieStore
//        httpClientBuilder.setDefaultCookieStore(cookieStore);
        // HttpClient
        CloseableHttpClient httpClient = httpClientBuilder.build();
        String response = "";
        Properties result = new Properties();
        try{
            httpresponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpresponse.getEntity();
            if(httpEntity != null) {
                response = EntityUtils.toString(httpEntity, "utf-8");
                Gson gson = new Gson();
                result = gson.fromJson(response,Properties.class);
            }
            logger.info("get configuration successfully, ["+result.size() +"] lines received");
        }catch(ClientProtocolException e){
            logger.error("http request failed，uri: "+url+",exception: "+e.getMessage());
        }catch(IOException e){
            logger.error("http request failed，uri: "+url+",exception: "+e.getMessage());
        }
        return result;
    }
}
