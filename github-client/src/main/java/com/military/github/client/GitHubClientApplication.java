package com.military.github.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableOAuth2Sso
@SpringBootApplication
@EnableResourceServer
public class GitHubClientApplication {
    public static void main(String[]args){
        SpringApplication.run(GitHubClientApplication.class,args);
    }

  @RequestMapping("/")
  public String home() {
    return "hello world";
  }
    @Autowired
    private OAuth2RestOperations restTemplate;

    @RequestMapping("/relay")
    public String relay() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("https://foo.com/bar", String.class);
        return "Success! (" + response.getBody() + ")";
    }
}