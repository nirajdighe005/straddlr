package com.straddler.controller;

import com.straddle.dataobject.IRootObject;
import com.straddle.serviceInterface.IPlaceOrders;
import com.straddler.services.PlaceOrders;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class PlaceOrdersController extends BaseController {

    @Autowired
    PlaceOrders placeOrders;

    @Override
    public IRootObject pivot(IRootObject rootObject) throws IOException, KiteException {
        return placeOrders.main(rootObject);
    }
}
