package com.straddler.services;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Position;
import com.zerodhatech.models.Quote;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    Map<SellType, String> tradingSymbols;

    @SneakyThrows
    @Override
    @Scheduled(fixedDelay = 60000,initialDelay = 30000)
    public void run() {
        if (tradingSymbols.isEmpty()) {
            Logger.log("NO SHARES IN POSITION TO CHECK");
            return;
        }
        Map<SellType, Quote> quotes = straddleUtils.getStraddleQuotes(tradingSymbols);
        int skew = straddleUtils.calculateStraddleSkew(quotes.get(CE), quotes.get(PE));
        sell(skew, PE);
        sell(skew, CE);
    }

    private void sell(int skew, SellType sellType) throws IOException, KiteException {
        Optional<Position> positionOption = getPosition(sellType);
        if (positionOption.isEmpty()) {
            Logger.log("NO SHARES IN POSITION TO CHECK");
            System.exit(0);
        }
        Position position = positionOption.get();
        //TODO: verify these fields
        double tradeValue = position.lastPrice - position.buyPrice;
        boolean shouldSell = tradeValue > maximumProfit || skew >= skewRatio;
        if (shouldSell) {
            orderUtils.squareOff(position);
        }
    }

    private Optional<Position> getPosition(SellType sellType) throws IOException, KiteException {
        Map<String, List<Position>> positions = kiteConnect.getPositions();
        List<Position> day = positions.getOrDefault("day", new ArrayList<>());
        if (day.isEmpty()) {
            return Optional.empty();
        }
        String tradingSymbol = tradingSymbols.get(sellType);
        return day.stream().filter(x -> x.tradingSymbol.equals(tradingSymbol)).findFirst();
    }
}
