package com.example.whatup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WhatUpBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhatUpBackEndApplication.class, args);
    }

}
