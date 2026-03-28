package com.store;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.store.mapper")
public class StoreUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreUserApplication.class, args);
    }

}
