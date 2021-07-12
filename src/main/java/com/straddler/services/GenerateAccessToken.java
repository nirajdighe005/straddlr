package com.straddler.services;

import com.straddle.abstrct.BasicService;
import com.straddle.dataobject.IRootObject;
import com.straddle.serviceInterface.IGenerateAccessToken;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
/*
        User user = kiteConnect.generateSession(request_token, api_secret);
        Logger.log("access_token : " + user.accessToken);
        Logger.log("public token : " + user.publicToken);*/

        kiteConnect.setAccessToken("o6IG8wqmcfl0Jdkd4mR19rtM2vMDGu8c");
        return null;
    }
}

