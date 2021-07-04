package com.straddler.services;

import com.straddle.abstrct.BasicService;
import com.straddle.dataobject.IRootObject;
import com.straddle.serviceInterface.IGenerateAccessToken;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.util.Properties;

@Component
public class GenerateAccessToken extends BasicService implements IGenerateAccessToken {

    @Value("${trade.api.api_secret}")
    public String api_secret;
    @Value("${trade.api.request_token}")
    public String request_token;

    @Autowired
    public KiteConnect kiteConnect;

    @Override
    public IRootObject main(IRootObject root) throws Throwable {

        User user = kiteConnect.generateSession(request_token, api_secret);

        System.out.println("access_token : " + user.accessToken);
        System.out.println("public token : " + user.publicToken);

        Properties props = new Properties();
        FileOutputStream out = new FileOutputStream("access_token.properties");
        props.setProperty("access_token", user.accessToken);
        props.store(out, null);
        out.close();
        return null;
    }
}

