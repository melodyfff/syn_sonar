package com.xinchen.syn_sonar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SynSonarApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynSonarApplication.class, args);
    }
}
