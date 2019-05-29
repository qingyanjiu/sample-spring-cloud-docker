package louis.demo.serviceconsumer.controller;

import java.util.HashMap;
import java.util.Map;

import louis.demo.serviceconsumer.restService.HelloService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import louis.demo.serviceconsumer.ContainAnnotationValueList;

@Controller
@RequestMapping("/")
public class ConsumerController implements ApplicationContextAware {
	
    private ApplicationContext applicationContext;

    @Given(value = "a given value")
    private String data = "";
    
    @Autowired
    HelloService helloService;

    @Value("${test.value}")
    private String test;

    @ResponseBody
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public Map helloConsumer() {
		Map hello = helloService.hello();
		System.out.println("HELLO***********"+hello);
        Map map = new HashMap();
        map.put("value",data);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/containAnnotationValueList", method = RequestMethod.GET)
    public ContainAnnotationValueList containAnnotationValueList() {
        return (ContainAnnotationValueList) applicationContext.getBean("containAnnotationValueList");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
