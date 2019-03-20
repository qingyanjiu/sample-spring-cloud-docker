package com.cisco.eagleeye.frontend.utils;

import org.apache.commons.lang.StringEscapeUtils;
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
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//@Component
public class SidecarPropertyConfigurer extends PropertySourcesPlaceholderConfigurer {

    private static Logger logger = Logger.getLogger(SidecarPropertyConfigurer.class);

    private static final String PROFILE_PROD = "prod";
    private static final String PROFILE_DEV = "dev";
    private static final String PROFILE_LOCAL = "local";

    private static final String SIDECAR_CONFIG_URL="http://localhost:30001/properties/";
    private static final String SIDECAR_CONFIG_USERNAME="eagleeye";
    private static final String SIDECAR_CONFIG_PASSWORD="eagleeye2019";
    private static final String PROJECT_NAME="frontend";

    private HttpClientBuilder httpClientBuilder;

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Properties mergeProperties()
    {
        String url = environment.getProperty("config.server.url");
        String profile = environment.getProperty("spring.profiles.active");
        if(url == null){
            url = SIDECAR_CONFIG_URL;
        }
        Properties result = new Properties();
        if(profile == null) {
            logger.error("Not found profile, set property spring.profiles.active to 'local' automatically");
            profile = PROFILE_LOCAL;
        } else {
            url += "/";
            url += PROJECT_NAME;
            url += "-";
            url += profile;
            url += ".properties";
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpresponse = null;

            logger.info("prepare to get configuration from[" + url + "]");

            if (httpClientBuilder == null) {
                // 创建HttpClientBuilder
                httpClientBuilder = HttpClientBuilder.create();
            }
            // BasicAuth
            CredentialsProvider provider = new BasicCredentialsProvider();
            // Create the authentication scope
            AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
            // Create credential pair
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(SIDECAR_CONFIG_USERNAME, SIDECAR_CONFIG_PASSWORD);
            // Inject the credentials
            provider.setCredentials(scope, credentials);
            // Set the default credentials provider
            httpClientBuilder.setDefaultCredentialsProvider(provider);
//        // Set the default cookieStore
//        httpClientBuilder.setDefaultCookieStore(cookieStore);
            // HttpClient
            CloseableHttpClient httpClient = httpClientBuilder.build();
            String response = "";
            try {
                httpresponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpresponse.getEntity();
                if (httpEntity != null) {
                    response = EntityUtils.toString(httpEntity, "utf-8");
//                    result = JSON.parseObject(response, Properties.class);
                    String[] lines = response.split("\n");
                    for (int i = 0; i < lines.length; i++) {
                        String line = lines[i];
                        String key = line.substring(0, line.indexOf(":"));
                        String value = line.substring(line.indexOf(":") + 1, line.length());
                        result.put(key.trim(), StringEscapeUtils.unescapeJava(value).trim());
                    }
                }
                logger.info("get configuration successfully, [" + result.size() + "] lines received");
            } catch (ClientProtocolException e) {
                logger.error("http request failed，uri: " + url + ",exception: " + e.getMessage());
            } catch (IOException e) {
                logger.error("http request failed，uri: " + url + ",exception: " + e.getMessage());
            }
        }

        if(this.localOverride) {
            Properties properties = new Properties();
            try {
                properties = PropertiesLoaderUtils.loadAllProperties("application-" + profile + ".properties");
            } catch (IOException e) {
                e.printStackTrace();
            }
            result.putAll(properties);
            logger.info("localOverride enabled , put local config to result");
        }
        return result;
    }
}
