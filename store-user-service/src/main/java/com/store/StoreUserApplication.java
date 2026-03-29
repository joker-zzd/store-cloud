package com.store;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.store.mapper")
@EnableFeignClients
public class StoreUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreUserApplication.class, args);
    }

}