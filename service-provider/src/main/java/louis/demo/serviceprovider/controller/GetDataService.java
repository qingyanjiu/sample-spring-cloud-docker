package louis.demo.serviceprovider.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "GATEWAY")
public interface GetDataService {
    @RequestMapping(value = "/test")
    String test();
}
