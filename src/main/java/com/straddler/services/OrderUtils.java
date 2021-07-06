package com.straddler.services;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderUtils {

    @Autowired
    KiteConnect kiteConnect;

    @Value("${trade.order_parameter.lot_size}")
    protected int lot_size;

    public Order placeBasicSellOrder(String tradingSymbol) throws KiteException, IOException {
        OrderParams orderParams = new OrderParams();
        orderParams.quantity = 75 * lot_size;
        orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
        orderParams.tradingsymbol = tradingSymbol;
        orderParams.product = Constants.PRODUCT_MIS;
        orderParams.exchange = Constants.EXCHANGE_NFO;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.price = 100.0;
        orderParams.triggerPrice = 0.0;
        orderParams.tag = "sell_";
        orderParams.trailingStoploss = 1.0;//tag is optional and it cannot be more than 8 characters and only alphanumeric is allowed

        return kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
    }

    public Order placeStopLossOrder(String tradingSymbol) throws IOException, KiteException {
        OrderParams orderParams = new OrderParams();
        orderParams.quantity = 75 * lot_size;
        orderParams.orderType = Constants.ORDER_TYPE_SL;
        orderParams.tradingsymbol = tradingSymbol;
        orderParams.product = Constants.PRODUCT_CNC;
        orderParams.exchange = Constants.EXCHANGE_NFO;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.price = 100.0;
        orderParams.triggerPrice = 0.0;
        orderParams.tag = "stpLss_"; //t
        orderParams.trailingStoploss = 1.0;
        return kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
    }

    public void squareOff(Position position) {

    }

}
