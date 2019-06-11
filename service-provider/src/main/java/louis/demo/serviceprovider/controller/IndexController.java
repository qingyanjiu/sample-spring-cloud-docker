package louis.demo.serviceprovider.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@RestController
public class IndexController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired(required = false)
    private GetDataService getDataService;

    @Value("${server.port}")
    private String port;

    @Value("${sidecar.health-uri}")
    private String uri;

    @RequestMapping(value="/hello", method= RequestMethod.GET)
    public Map index(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map map = new HashMap();
        map.put("result","success");
        map.put("profile",env.getActiveProfiles()[0]);
        map.put("server.port",port);
        map.put("sidecar.health-uri",uri);
        return map;
    }

    @RequestMapping(value="/test")
    public String test(){
        String result = getDataService.test();
        return result;
    }

    @RequestMapping(value="/fallback")
    @HystrixCommand(fallbackMethod = "fallback")
    public String hello(){
        String result = restTemplate.getForObject("http://gateway/test",String.class);
        return result;
    }

    private String fallback(){
        return "fallback";
    }


    @RequestMapping("testProperty")
    public String testProperty(){
        return this.env.getProperty("sidecar.password");
    }
}
