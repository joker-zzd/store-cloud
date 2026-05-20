package com.store;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.store")
@MapperScan("com.store.mapper")
@EnableScheduling
public class StoreProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreProductApplication.class, args);
    }

}
