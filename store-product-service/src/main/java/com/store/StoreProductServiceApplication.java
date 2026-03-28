package com.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.store.mapper")
public class StoreProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreProductServiceApplication.class, args);
    }

}
