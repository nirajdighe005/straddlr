package com.straddler.services;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.straddler.services.SellType.CE;
import static com.straddler.services.SellType.PE;

@Component
public class StraddleUtils {


    public static final String INSTRUMENT_SEGMENT = "NFO-OPT";
    public static final String INSTRUMENT_NAME = "NIFTY";
    public static final String EXCHANGE = "NFO";
    public static final String EXCHANGE_MARKET = "NSE:NIFTY 50";
    @Autowired
    KiteConnect kiteConnect;

    @Value("${trade.straddle.tradingSymbol}")
    protected String tradingSymbol;

    @Autowired
    Map<SellType, String> tradingSymbols;

    @Autowired
    DateTimeUtils dateTimeUtils;

    public Map<SellType, Quote> getStraddleQuotes(Map<SellType, String> tradingSymbols) throws IOException, KiteException {
        Map<String, Quote> quotes = kiteConnect.getQuote(tradingSymbols.values().toArray(String[]::new));
        Map<SellType, Quote> sellingQuotes = new HashMap<>();

        for (Map.Entry<SellType, String> tradingSymbol : tradingSymbols.entrySet()) {
            if (tradingSymbol.getKey().equals(PE)) {
                sellingQuotes.put(PE,quotes.get(tradingSymbol.getValue()));
            } else if (tradingSymbol.getKey().equals(CE)) {
                sellingQuotes.put(CE,quotes.get(tradingSymbol.getValue()));
            }
        }
        return sellingQuotes;
    }

    public int calculateStraddleSkew(Quote pe, Quote ce) {
        if (pe != null && ce != null) {
            double price1 = pe.lastPrice;
            double price2 = ce.lastPrice;
            return (int) Math.round((Math.abs(price1 - price2) / ((price1 + price2) / 2)) * 100);
        }
        return Integer.MAX_VALUE;
    }

    public Map<SellType, String> getStraddleTradingSymbols() throws IOException, KiteException {
        if (tradingSymbols.isEmpty()) {
            Date expiry = dateTimeUtils.getNextThursday();
            long atmStrike = getAtmStrike();
            List<Instrument> nfoInstruments = getStraddleInstruments(expiry, atmStrike);
            for (Instrument nfoInstrument : nfoInstruments) {
                if (nfoInstrument.getTradingsymbol().contains(SellType.CE.name())) {
                    tradingSymbols.put(SellType.CE, tradingSymbol);
                } else if (nfoInstrument.getTradingsymbol().contains(SellType.PE.name())) {
                    tradingSymbols.put(SellType.PE, tradingSymbol);
                }
            }
        }
        return tradingSymbols;
    }

    private List<Instrument> getStraddleInstruments(Date expiry, long atmStrike) throws KiteException, IOException {
        List<Instrument> nfo = kiteConnect.getInstruments(EXCHANGE);
        return nfo.stream()
                .filter(x -> x.getSegment().equals(INSTRUMENT_SEGMENT))
                .filter(x ->  x.getName().equals(INSTRUMENT_NAME))
                .filter(x -> x.getStrike().equals(String.valueOf(atmStrike)))
                .filter(x -> x.getExpiry().equals(expiry))
                .collect(Collectors.toList());
    }

    private long getAtmStrike() throws KiteException, IOException {
        Map<String, Quote> quote = kiteConnect.getQuote(new String[]{EXCHANGE_MARKET});
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
}
