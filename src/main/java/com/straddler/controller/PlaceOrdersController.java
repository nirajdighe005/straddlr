package com.straddler.controller;

import com.straddle.dataobject.IRootObject;
import com.straddler.services.PlaceOrders;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/placeOrders")
public class PlaceOrdersController extends BaseController {

    @Autowired
    PlaceOrders placeOrders;

    @Override
    @GetMapping(value = "/t1")
    public IRootObject pivot(IRootObject rootObject) throws IOException, KiteException {
        return placeOrders.main(rootObject);
    }
}
