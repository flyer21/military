package com.military.service.container;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class ModuleApplicationAutoConfiguration {
    public ModuleApplicationAutoConfiguration(){
    }


    @Bean
    public MilitaryHandlerMapping militrayHandlerMapping(){
        return new MilitaryHandlerMapping();
    }
    @Bean
    public ModuleApplicationContainer  moduleApplicationContainer(){
        return new ModuleApplicationContainer();
    }
    @Bean
    ModuleApplicationContainerStarter ModuleApplicationContainerRunner(){
        return new ModuleApplicationContainerStarter();
    }


}