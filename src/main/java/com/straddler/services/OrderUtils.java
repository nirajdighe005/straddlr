package com.straddler.services;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;
import com.zerodhatech.models.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class OrderUtils {

    @Autowired
    KiteConnect kiteConnect;

    @Value("${trade.order_parameter.number_of_lots}")
    protected int number_of_lots;

    @Value("${trade.order_parameter.lot_quantity}")
    protected int lot_quantity;

    public Order placeBasicOrder(String tradingSymbol, String transactionType) throws KiteException, IOException {
        OrderParams orderParams = new OrderParams();
        orderParams.quantity = lot_quantity * number_of_lots;
        orderParams.orderType = Constants.ORDER_TYPE_MARKET;
        orderParams.tradingsymbol = tradingSymbol;
        orderParams.product = Constants.PRODUCT_MIS;
        orderParams.exchange = Constants.EXCHANGE_NFO;
        orderParams.transactionType = transactionType;
        orderParams.validity = Constants.VALIDITY_DAY;
  //      orderParams.price = 100.0;
  //      orderParams.triggerPrice = 0.0;
        orderParams.tag = "sell";
//        orderParams.trailingStoploss = 1.0;//tag is optional and it cannot be more than 8 characters and only alphanumeric is allowed

        return kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
    }

    public Order placeStopLossOrder(String tradingSymbol, Double averagePrice) throws IOException, KiteException {
        OrderParams orderParams = new OrderParams();
        orderParams.quantity = lot_quantity * number_of_lots;
        orderParams.orderType = Constants.ORDER_TYPE_SLM;
        orderParams.tradingsymbol = tradingSymbol;
        orderParams.product = Constants.PRODUCT_MIS;
        orderParams.exchange = Constants.EXCHANGE_NFO;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
        orderParams.validity = Constants.VALIDITY_DAY;
        DecimalFormat df = new DecimalFormat("#.#");
        orderParams.triggerPrice = Double.valueOf(df.format(averagePrice * getTodaysStopLoss()));
        orderParams.tag = "stpLss_"; //t
        return kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
    }

    private Double getTodaysStopLoss() {
        return CommonConstants.STOP_LOSS.get(LocalDate.now().getDayOfWeek());
    }

    public Optional<Position> getPosition(String tradingSymbol) throws IOException, KiteException {
        Map<String, List<Position>> positions = kiteConnect.getPositions();
        List<Position> day = positions.getOrDefault("net", new ArrayList<>());
        if (day.isEmpty()) {
            return Optional.empty();
        }
        return day.stream().filter(x -> x.tradingSymbol.equals(tradingSymbol)).findFirst();
    }

    public Order getOrder(String orderId) throws IOException, KiteException {
        List<Order> orders = kiteConnect.getOrders();
        Optional<Order> orderOptional = orders.stream().filter(x -> x.orderId.equals(orderId)).findFirst();
        if (orderOptional.isEmpty()) {
            return null;
        }
        Order order = orderOptional.get();
        if(!order.status.equals(CommonConstants.ORDER_STATUS_COMPLETE)){
            Logger.log(order.status);
            return null;
        }
        return order;
    }

    public List<Order> getOrders() throws IOException, KiteException {
        return kiteConnect.getOrders();

    }
}



