package com.coding.studiodemo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.ModelMap;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import com.coding.studiodemo.filters.PreFilter;

@SpringBootApplication
@Controller
@EnableZuulProxy
public class StudioDemoApplication {
    @RequestMapping("/")
    public String greeting(ModelMap map) {
        String jreVersion = System.getProperty("java.specification.version");
        map.addAttribute("jreVersion", "v" + jreVersion);
        return "index";
    }

    public static void main(String[] args) {
        SpringApplication.run(StudioDemoApplication.class, args);
    }

    @Bean
    public PreFilter preFilter() {
        return new PreFilter();
    }
}