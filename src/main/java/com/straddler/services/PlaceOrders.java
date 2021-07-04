package com.straddler.services;

import com.straddle.abstrct.BasicService;
import com.straddle.dataobject.IRootObject;
import com.straddle.serviceInterface.IPlaceOrders;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.OrderParams;
import com.zerodhatech.models.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PlaceOrders extends BasicService implements IPlaceOrders {

    @Autowired
    protected KiteConnect kiteConnect;

    @Value("${trade.order_parameter.skewRatio}")
    protected int skewRatio;

    @Value("${trade.straddle.tradingSymbol}")
    protected String tradingSymbol;

    @Value("${trade.order_parameter.lot_size}")
    protected int lot_size;

    public final Map<DayOfWeek, Double> stopLossMap = Map.of(DayOfWeek.MONDAY, 1.3,
            DayOfWeek.TUESDAY, 1.3,
            DayOfWeek.WEDNESDAY, 1.4,
            DayOfWeek.THURSDAY, 1.7,
            DayOfWeek.FRIDAY, 1.3);

    public final List<LocalDate> holidays = Collections.singletonList(LocalDate.of(2021, 8, 19));

    public IRootObject main(IRootObject root) throws IOException, KiteException {
        setAccessToken();
        Date nextThursday = getNextThursday();
        long atmStrike = getAtmStrike();
        List<Instrument> instruments = getInstruments(nextThursday, atmStrike);
        int skew = calculateSkew(instruments);
        System.out.println(skew);
        if (skew < skewRatio) {
            purchase(instruments);
        }
        return null;
    }

    private void purchase(List<Instrument> instruments) throws IOException, KiteException {

        for (Instrument instrument : instruments) {
            placeBasicSellOrder(instrument);
        }

    }

    private void placeBasicSellOrder(Instrument instrument) throws KiteException, IOException {
        OrderParams orderParams = new OrderParams();
        orderParams.quantity = 75 * lot_size;
        orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
        orderParams.tradingsymbol = instrument.getTradingsymbol();
        orderParams.product = Constants.PRODUCT_MIS;
        orderParams.exchange = Constants.EXCHANGE_NFO;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.price = 100.0;
        orderParams.triggerPrice = 0.0;
        orderParams.tag = "stradd"; //tag is optional and it cannot be more than 8 characters and only alphanumeric is allowed

        kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
    }

    private int calculateSkew(List<Instrument> instruments) throws IOException, KiteException {
        Map<String, String> tradingSymbols = instruments.stream().collect(Collectors.toMap(y -> String.valueOf(y.getInstrument_token()), Instrument::getTradingsymbol));
        Map<String, Quote> quotes = kiteConnect.getQuote(tradingSymbols.keySet().toArray(String[]::new));
        Quote pe = null, ce = null;

        for (String instrumentToken : quotes.keySet()) {
            if (tradingSymbols.get(instrumentToken).contains("PE")) {
                pe = quotes.get(instrumentToken);
            } else if (tradingSymbols.get(instrumentToken).contains("CE")) {
                ce = quotes.get(instrumentToken);
            }
        }
        if (pe != null && ce != null) {
            double price1 = pe.lastPrice;
            double price2 = ce.lastPrice;
            return (int) Math.round((Math.abs(price1 - price2) / ((price1 + price2) / 2)) * 100);
        }
        return Integer.MAX_VALUE;
    }

    private List<Instrument> getInstruments(Date filterDate, Long atmStrike) throws IOException, KiteException {
        List<Instrument> nfo = kiteConnect.getInstruments("NFO");
        return nfo.stream()
                .filter(x -> x.getSegment().equals("NFO-OPT"))
                .filter(x -> x.getName().equals("NIFTY"))
                .filter(x -> x.getStrike().equals(atmStrike.toString()))
                .filter(x -> x.getExpiry().equals(filterDate))
                .collect(Collectors.toList());
    }

    private Date getNextThursday() {
        LocalDate ld = LocalDate.now();
        LocalDate nextThursday = ld.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
        if (holidays.contains(nextThursday)) {
            nextThursday = ld.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
        }
        return Date.from(nextThursday.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private long getAtmStrike() throws KiteException, IOException {
        Map<String, Quote> quote = kiteConnect.getQuote(new String[]{"NSE:NIFTY 50"});
        double lastPrice = quote.get(tradingSymbol).lastPrice;
        long round = Math.round(lastPrice);
        long rem = round % 100;
        long quo = round / 100;
        if (rem > 75) {
            return quo * 100 + 100;
        } else if (rem > 25) {
            return quo * 100 + 50;
        } else {
            return quo * 100;
        }
    }

    private void setAccessToken() throws IOException {
        FileInputStream in = new FileInputStream("access_token.properties");
        Properties props = new Properties();
        props.load(in);
        in.close();
        String access_token = props.getProperty("access_token");
        kiteConnect.setAccessToken(access_token);
    }
}
