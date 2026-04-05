package com.store;

import com.store.config.FileStorageProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan("com.store.mapper")
@EnableConfigurationProperties(FileStorageProperties.class)
public class StoreFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreFileApplication.class, args);
    }

}
