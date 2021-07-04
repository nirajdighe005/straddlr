package com.straddler.controller;

import com.straddle.dataobject.IRootObject;
import com.straddle.dataobject.RootObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestController extends BaseController {

    @GetMapping(value = "/t1")
    public IRootObject pivot(IRootObject rootObject) {

        return new RootObject();
    }
}
