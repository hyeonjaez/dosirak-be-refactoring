package com.example.dosirakbe;


import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableEncryptableProperties
@EnableJpaAuditing
@SpringBootApplication
public class DosirakBeApplication {

    public static void main(String[] args) {

        SpringApplication.run(DosirakBeApplication.class, args);
    }

}
