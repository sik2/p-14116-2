package com.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.back")
@EnableJpaAuditing
public class MarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarketApplication.class, args);
    }
}
