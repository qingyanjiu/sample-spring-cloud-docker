package louis.demo.serviceconsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@RequestMapping("/")
public class ConsumerController {
    @Autowired
    RestTemplate restTemplate;

    @ResponseBody
    @RequestMapping(value = "/consumer", method = RequestMethod.GET)
    public Map helloConsumer() {
        return restTemplate.getForEntity("http://HELLO-SERVICE/hello",Map.class).getBody();
    }
}
