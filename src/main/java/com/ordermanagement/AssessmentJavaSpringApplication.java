package com.ordermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class AssessmentJavaSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssessmentJavaSpringApplication.class, args);
    }

}
