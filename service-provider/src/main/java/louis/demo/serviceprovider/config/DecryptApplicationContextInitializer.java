package louis.demo.serviceprovider.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于读取所有获得的配置信息，将其中以(ENCODE)开头的配置解密
 * 一般适用于密码
 * 具体请看我的笔记文档
 * https://onedrive.live.com/redir?resid=4E4FEDCC2A0CC9B9%21883&authkey=%21AI89Vcpo5Hygs18&page=Edit&wd=target%28%E7%AA%81%E7%84%B6%E6%83%B3%E5%88%B0%E7%9A%84%E9%97%AE%E9%A2%98%E8%AE%B0%E5%BD%95%EF%BC%8C%E5%9B%9E%E5%A4%B4%E6%89%BE%E8%B5%84%E6%96%99%E8%A7%A3%E7%AD%94.one%7C33a86a40-ab11-4e49-bae0-11824c612694%2Fspringboot%E4%BB%A5%E5%8F%8Aspringcloud%E9%A1%B9%E7%9B%AE%E9%85%8D%E7%BD%AE%E8%87%AA%E5%AE%9A%E4%B9%89%E8%A7%A3%E5%AF%86%E9%85%8D%E7%BD%AE%E4%BF%A1%E6%81%AF%7Ca1444736-1ae0-4cf3-9617-16561b5d05a5%2F%29&wdorigin=703
 */

public class DecryptApplicationContextInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        PropertySource propertySource = propertySources.get("configurationProperties");
        Map passwordMap = new HashMap();
        for (PropertySource<?> source : environment.getPropertySources()) {
            if (source instanceof EnumerablePropertySource) {
                for (String key : ((EnumerablePropertySource) source).getPropertyNames()) {
                    String value = source.getProperty(key).toString();
                    if(value !=null && value.startsWith("(ENCODE)")) {
                        //TODO 解密
                        String decryptedStr = "解密后字符串";
                        passwordMap.put(key, decryptedStr);
                    }
                }
            }
        }
        CompositePropertySource composite = new CompositePropertySource("decrypt");
        MapPropertySource addedPropertySource = new MapPropertySource("decryptProperties", passwordMap);
        composite.addPropertySource(addedPropertySource);
        propertySources.addFirst(composite);
    }
}
