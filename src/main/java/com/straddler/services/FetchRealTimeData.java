package com.straddler.services;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.models.Position;
import com.zerodhatech.models.Quote;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static com.straddler.services.SellType.CE;
import static com.straddler.services.SellType.PE;

@Component
public class FetchRealTimeData implements Runnable {

    @Autowired
    OrderUtils orderUtils;

    @Autowired
    StraddleUtils straddleUtils;

    @Value("${trade.order_parameter.skewRatio}")
    protected int skewRatio;

    @Value("${trade.order_parameter.maximumProfit}")
    protected double maximumProfit;

    @Autowired
    protected KiteConnect kiteConnect;

    @SneakyThrows
    @Override
    public void run() {
        Optional<Position> positionOption = getPosition();
        if (positionOption.isEmpty()) {
            System.exit(0);
        } else {
            Position position = positionOption.get();
            double tradeValue = position.lastPrice - position.buyPrice;
            if (tradeValue > maximumProfit) {
                orderUtils.squareOff(position);
            }
            Map<SellType, String> tradingSymbols = straddleUtils.getStraddleTradingSymbols();
            Map<SellType, Quote> quotes = straddleUtils.getStraddleQuotes(tradingSymbols);
            int skew = straddleUtils.calculateStraddleSkew(quotes.get(CE), quotes.get(PE));

            if (skew >= skewRatio) {
                orderUtils.squareOff(position);
            }
        }
    }

    private Optional<Position> getPosition() {
        return ;
    }
}
