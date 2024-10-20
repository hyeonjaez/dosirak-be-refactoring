package com.example.dosirakbe;

import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DosirakBeApplication {

    public static void main(String[] args) {
        System.out.println();



        SpringApplication.run(DosirakBeApplication.class, args);
    }

}
