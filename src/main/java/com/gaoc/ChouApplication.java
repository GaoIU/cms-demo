package com.gaoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description: 启动类
 * @Date: 2020/7/1 10:29
 * @Author: Gaoc
 */
@EnableScheduling
@ConfigurationPropertiesScan("com.gaoc.**.properties")
@SpringBootApplication
public class ChouApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChouApplication.class, args);
    }

}
