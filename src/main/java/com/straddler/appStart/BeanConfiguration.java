package com.straddler.appStart;

import com.straddler.services.GenerateAccessToken;
import com.straddler.services.SellType;
import com.zerodhatech.kiteconnect.KiteConnect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class BeanConfiguration {

    @Value("${trade.api.api_key}")
    public String apiKey;

    @Bean
    public KiteConnect getKiteConnect() {
        return new KiteConnect(apiKey);
    }

    @Bean
    public Map<SellType, String> getTradingSymbols() {
        return new HashMap<>();
    }

}
