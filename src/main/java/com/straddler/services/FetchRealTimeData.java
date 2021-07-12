package com.straddler.services;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.Position;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FetchRealTimeData implements Runnable {

    @Autowired
    OrderUtils orderUtils;

    @Autowired
    StraddleUtils straddleUtils;
    @Value("${trade.order_parameter.maximumProfit}")
    protected double maximumProfit;

    @Autowired
    protected KiteConnect kiteConnect;

    @Autowired
    Map<SellType, String> tradingSymbols;

    @SneakyThrows
    @Override
    //@Scheduled(fixedDelay = 60000,initialDelay = 30000)
    public void run() {
        if (tradingSymbols.isEmpty()) {
            Logger.log("NO SHARES IN POSITION TO CHECK");
            return;
        }

        boolean shouldSquareOffPE = true;
        boolean shouldSquareOffCE = true;

        //isOneStopLossHit();
  /*      if(shouldSquareOff()){

        }*/
        if (shouldSquareOffPE) {
            squareOff(SellType.PE);
        }
        if (shouldSquareOffCE) {
            squareOff(SellType.CE);
        }
    }

/*    private boolean shouldSquareOff() {

    }*/

    private Order isOneStopLossHit() throws KiteException, IOException {
        List<Order> slmOrders = kiteConnect
                .getOrders()
                .stream()
                .filter(order -> order.orderType.equals(Constants.ORDER_TYPE_SLM))
                .filter(order -> order.status.equals(CommonConstants.ORDER_STATUS_OPEN))
                .collect(Collectors.toList());

        if (slmOrders.size() == 1) {
            return slmOrders.get(0);
        }
        return null;
    }

    private void squareOff(SellType sellType) throws IOException, KiteException {
        String tradingSymbol = tradingSymbols.get(sellType);
        orderUtils.placeBasicOrder(tradingSymbol, Constants.TRANSACTION_TYPE_SELL);

        /*
        Optional<Position> positionOption = orderUtils.getPosition(tradingSymbol);

        if (positionOption.isEmpty()) {
            Logger.log("NO SHARES IN POSITION TO CHECK");
            System.exit(0);
        }
        Position position = positionOption.get();
        boolean shouldSell = position.m2m > maximumProfit;
        if (shouldSell) {
            orderUtils.squareOff(position);
        }
        */
    }
}
