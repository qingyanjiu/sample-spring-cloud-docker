package louis.demo.eurekaclient.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "API-GATEWAY")
public interface GetDataService {
    @RequestMapping(value = "/test")
    String test();
}
