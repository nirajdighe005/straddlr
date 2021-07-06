package com.straddler.appStart;

import com.straddle.dataobject.RootObject;
import com.straddler.services.GenerateAccessToken;
import com.straddler.services.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

@SpringBootApplication
@ComponentScan({"com.straddler.controller", "com.straddler.services","com.straddler.appStart"})
public class StraddlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StraddlerApplication.class, args);
    }

}
