package com.sbs.demo5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class Demo5Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo5Application.class, args);
    }

}
