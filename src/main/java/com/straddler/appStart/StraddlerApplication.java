package com.straddler.appStart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StraddlerApplication {

    public static void main(String[] args) {

        System.out.println("This is spring boot application");
        SpringApplication.run(StraddlerApplication.class, args);
    }

}
