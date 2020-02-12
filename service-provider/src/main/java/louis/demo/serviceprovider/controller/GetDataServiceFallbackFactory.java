/**
 * @author louisliu
 */

package louis.demo.serviceprovider.controller;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class GetDataServiceFallbackFactory implements FallbackFactory<GetDataService> {

    @Override
    public GetDataService create(Throwable cause) {
        return new GetDataService() {
            @Override
            public String test() {
                return "gateway is down";
            }
        };
    }
}
