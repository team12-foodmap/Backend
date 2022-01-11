package com.example.foodmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class FoodMapApplication {
//    public static final String APPLICATION_LOCATIONS =
//            "spring.config.location="
//            + "classpath:/application.yml,"
//            + "classpath:/secret.yml";

    public static void main(String[] args) {

        SpringApplication.run(FoodMapApplication.class);
//        new SpringApplicationBuilder(FoodMapApplication.class)
//                .properties(APPLICATION_LOCATIONS)
//                .run(args);
    }
}
