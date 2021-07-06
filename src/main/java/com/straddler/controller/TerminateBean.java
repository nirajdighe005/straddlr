package com.straddler.controller;

import com.straddle.abstrct.BasicService;
import com.straddle.dataobject.IRootObject;
import com.straddle.serviceInterface.IBasicService;
import com.straddler.appStart.ApplicationStartup;
import com.straddler.services.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class TerminateBean extends BasicService {

    @PreDestroy
    public void onDestroy() throws Exception {
        System.out.println("Spring Container is destroyed!");
    }

    @Override
    public IRootObject main(IRootObject rootObject) {
        Logger.log("THE END");
        System.exit(0);
        return null;
    }
}
