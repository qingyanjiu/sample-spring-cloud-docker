package louis.demo;

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
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CustomConfigInitailizer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private final Logger logger = Logger.getLogger(getClass());

    private static final String PROFILE_PROD = "prod";
    private static final String PROFILE_DEV = "dev";
    private static final String PROFILE_LOCAL = "local";

    private static final String SIDECAR_CONFIG_URL="http://localhost:3000";
    private static final String SIDECAR_CONFIG_USERNAME="";
    private static final String SIDECAR_CONFIG_PASSWORD="";
    private static final String PROJECT_NAME="gateway";

    private static final String PROPERTY_SOURCE_NAME="eecustom";

    private HttpClientBuilder httpClientBuilder;

    @Autowired(required = false)
    private List<PropertySourceLocator> propertySourceLocators = new ArrayList<>();

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        Map properties = new LinkedHashMap<>();
        String url = environment.getProperty("config.server.url");
        String profile = environment.getProperty("spring.profiles.active");
        if(url == null){
            url = SIDECAR_CONFIG_URL;
        }
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
                        properties.put(key.trim(), StringEscapeUtils.unescapeJava(value).trim());
                    }
                }
                logger.info("get configuration successfully, [" + properties.size() + "] lines received");
            } catch (ClientProtocolException e) {
                logger.error("http request failed，uri: " + url + ",exception: " + e.getMessage());
            } catch (IOException e) {
                logger.error("http request failed，uri: " + url + ",exception: " + e.getMessage());
            }
        }

        CompositePropertySource composite = new CompositePropertySource(PROPERTY_SOURCE_NAME);
        MapPropertySource propertySource = new MapPropertySource("config", properties);
        composite.addPropertySource(propertySource);
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(composite);

    }
}
