package com.ecommerce.project.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    //Creating a bean/obj of ModelMapper to use in ServiceImp Class
    @Bean
    public ModelMapper createModelMapper(){
        return new ModelMapper();
    }
}
