package com.straddler.services;

import com.straddle.abstrct.BasicService;
import com.straddle.dataobject.IRootObject;
import com.straddle.serviceInterface.IPlaceOrders;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.Position;
import com.zerodhatech.models.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.straddler.services.SellType.*;

@Component
public class PlaceOrders extends BasicService implements IPlaceOrders {
    @Value("${trade.order_parameter.skewRatio}")
    protected int skewRatio;

    @Autowired
    OrderUtils orderUtils;

    @Autowired
    StraddleUtils straddleUtils;

    @Autowired
    Map<SellType, String> tradingSymbols;

    @Autowired
    KiteConnect kiteConnect;


    public IRootObject main(IRootObject root) throws IOException, KiteException {

/*
        Map<SellType, Long> instrumentToken = straddleUtils.initializeStraddleTradingSymbols();
        Map<SellType, Quote> quotes = straddleUtils.getStraddleQuotes(instrumentToken);
        int skew = straddleUtils.calculateStraddleSkew(quotes.get(CE), quotes.get(PE));
*/

        //if (skew < skewRatio) {

            Map<String, List<Position>> positions = kiteConnect.getPositions();

            Map<String, Position> stopLoss = positions.get("day").stream().collect(Collectors.toMap(x -> x.tradingSymbol, y -> y));

            for (Map.Entry<String, Position> stop : stopLoss.entrySet()) {
                Order value = orderUtils.placeBasicOrder(stop.getKey(), Constants.TRANSACTION_TYPE_SELL);
            }

        /*    for (Map.Entry<String, Position> stop : stopLoss.entrySet()) {
                orderUtils.placeStopLossOrder(stop.getKey(), stop.getValue().buyPrice);
            }
*/
       /*     for (Map.Entry<String, Order> stop : stopLoss.entrySet()) {
                Order value = stop.getValue();
                if (value != null && value.orderId != null) {
                    Order orderObj = orderUtils.getOrder(value.orderId);
                    if (orderObj != null) {
                        orderUtils.placeStopLossOrder(stop.getKey(), Double.valueOf(orderObj.averagePrice));
                    }
                }
            }*/

        return null;
    }
}
