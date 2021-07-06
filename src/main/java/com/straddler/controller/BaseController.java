package com.straddler.controller;

import com.straddle.dataobject.IRootObject;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;

import java.io.IOException;

public abstract class BaseController {
    public abstract IRootObject pivot(IRootObject rootObject) throws IOException, KiteException;
}
