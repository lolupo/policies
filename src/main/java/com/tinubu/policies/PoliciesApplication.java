package com.tinubu.policies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PoliciesApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoliciesApplication.class, args);
    }

}
