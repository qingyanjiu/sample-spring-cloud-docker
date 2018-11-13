package louis.demo.serviceconsumer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class ConsumerController {

    @Given(value = "a given value")
    private String data = "";
//    @Autowired
//    HelloService helloService;

    @ResponseBody
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public Map helloConsumer() {
//        return helloService.hello();
        Map map = new HashMap();
        map.put("value",data);
        return map;
    }
}
