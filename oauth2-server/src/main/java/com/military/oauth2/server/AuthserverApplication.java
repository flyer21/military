package com.military.oauth2.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.security.Principal;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@Controller
@EnableResourceServer
@SessionAttributes("authorizationRequest")
public class AuthserverApplication extends WebMvcConfigurerAdapter {
    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
    public static void main(String[] args) {
        SpringApplication.run(AuthserverApplication.class, args);
    }


}
