package com.example.paneli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableScheduling
@SpringBootApplication
@EnableAsync
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PaneliApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaneliApplication.class, args);
    }

}
