package com.store.storeauthservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class StoreAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreAuthApplication.class, args);
    }

}
