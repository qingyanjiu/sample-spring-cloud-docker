package louis.demo.serviceconsumer.restService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(name = "hello-service")
@Service
public interface HelloService {
    @RequestMapping("/hello")
    Map hello();
}
