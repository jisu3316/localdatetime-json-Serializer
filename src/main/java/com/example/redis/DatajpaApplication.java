package com.example.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class DatajpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatajpaApplication.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "hello";
    }

}
