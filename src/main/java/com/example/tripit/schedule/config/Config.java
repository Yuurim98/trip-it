package com.example.tripit.schedule.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    //체 간의 매핑을 자동으로 처리할 수 있도록
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
