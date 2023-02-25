package com.hungrybandits.rest.recipes;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients(
        basePackages = "com.hungrybandits.clients"
)
public class RecipeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecipeApplication.class, args);
    }
}

