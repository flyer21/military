package com.military.base.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.web.bind.annotation.RestController;


@EnableEurekaClient
@SpringBootApplication
@RestController
@EnableHystrix
public class MiltaryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiltaryServiceApplication.class, args);
    }

//    @Bean
//    public ApplicationRunner applicationRunner(ApplicationContext ctx){
//        return  cc ->{
//            System.out.println("==== begin load Modules. \n");
//            long st = System.currentTimeMillis();
//
//            ModuleContainer sc= new ModuleContainer(ctx);
//            sc.init();
//            sc.start();
//
//            System.out.printf("==== sucessed Load Modules, used %s ms.\n",(System.currentTimeMillis()-st));
//
//
//        };
//    }

}
