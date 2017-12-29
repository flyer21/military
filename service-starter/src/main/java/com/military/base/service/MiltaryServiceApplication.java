package com.military.base.service;

import com.military.service.container.ModuleApplicationContainer;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
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
    public String info(){
//        LOG.info("calling trace service-hi ");

        return "i'm service-hi1111";

    }
    @Bean
    public ApplicationRunner applicationRunner(ApplicationContext ctx){
        return  cc ->{
            System.out.println("==== begin load Modules. \n");
            long st = System.currentTimeMillis();

            ModuleApplicationContainer sc= new ModuleApplicationContainer(ctx);
            sc.init();
            sc.start();

            System.out.printf("==== sucessed Load Modules, used %s ms.\n",(System.currentTimeMillis()-st));


        };
    }

}
