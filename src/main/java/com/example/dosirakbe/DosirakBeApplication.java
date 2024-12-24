package com.example.dosirakbe;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableCaching
@SpringBootApplication
public class DosirakBeApplication {

    public static void main(String[] args) {

        SpringApplication.run(DosirakBeApplication.class, args);
    }

}
