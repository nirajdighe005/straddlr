package com.straddler.services;

import com.straddle.abstrct.BasicService;
import com.straddle.dataobject.IRootObject;
import com.straddle.serviceInterface.IPlaceOrders;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import static com.straddler.services.SellType.*;

@Component
public class PlaceOrders extends BasicService implements IPlaceOrders {
    @Value("${trade.order_parameter.skewRatio}")
    protected int skewRatio;

    @Autowired
    OrderUtils orderUtils;

    @Autowired
    StraddleUtils straddleUtils;


    public IRootObject main(IRootObject root) throws IOException, KiteException {

        Map<SellType, String> tradingSymbols = straddleUtils.initializeStraddleTradingSymbols();
        Map<SellType, Quote> quotes = straddleUtils.getStraddleQuotes(tradingSymbols);
        int skew = straddleUtils.calculateStraddleSkew(quotes.get(CE), quotes.get(PE));

        if (skew < skewRatio) {

            for (String tradingSymbol : tradingSymbols.values()) {
                orderUtils.placeBasicSellOrder(tradingSymbol);
                orderUtils.placeStopLossOrder(tradingSymbol);
            }
        }
        return null;
    }
}
