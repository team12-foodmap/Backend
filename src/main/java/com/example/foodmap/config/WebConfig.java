package com.example.foodmap.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/images/**")
//                .addResourceLocations("file:///home/ubuntu/images/");
                 .addResourceLocations("file:///Users/nayeongkim/Desktop"); // Local 컴퓨터
    }
}
