package louis.demo.serviceconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
public class ServiceConsumerApplication {

//	@Bean
//	@LoadBalanced//启用ribbon负载均衡调用服务
//	public RestTemplate restTemplate(){
//		return new RestTemplate();
//	}

	public static void main(String[] args) {
		SpringApplication.run(ServiceConsumerApplication.class, args);
	}
}
