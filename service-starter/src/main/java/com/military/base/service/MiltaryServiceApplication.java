package com.military.base.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@EnableEurekaClient
@SpringBootApplication
@RestController
@EnableHystrix
public class MiltaryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiltaryServiceApplication.class, args);
    }
    @RequestMapping("/ok")
    public String ok(){
//        LOG.info("calling trace service-hi ");
        return "i'm service-hi1111";

    }




}
