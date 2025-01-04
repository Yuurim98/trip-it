package com.example.tripit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig  implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .exposedHeaders("Set-Cookie")
                .allowedMethods("*")
                .allowedOrigins("http://172.16.1.122:3000")
//                .allowedOrigins("http://172.16.1.140:3001")
                //.allowedOrigins("http://localhost:3000")
                //.allowedOrigins("https://moonmoon96.github.io")
                .allowCredentials(true);
    }
}
