package com.straddler.appStart;

import com.straddle.dataobject.RootObject;
import com.straddler.controller.TerminateBean;
import com.straddler.services.DateTimeUtils;
import com.straddler.services.GenerateAccessToken;
import com.straddler.services.Logger;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    GenerateAccessToken generateAccessToken;

    @Autowired
    TerminateBean terminateBean;

    @Autowired
    DateTimeUtils dateTimeUtils;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        try {
            generateAccessToken.main(new RootObject());
            /*if (shouldTradeToday()) {
            }
            else {
                Logger.log("CANNOT TRADE TODAY OR AT CURRENT TIME");
                terminateBean.main(null);
            }*/
        } catch (Throwable throwable) {
            Logger.log("Could not generate access token. Please check the following exception.");
            throwable.printStackTrace();
            terminateBean.main(null);
        }
    }

    public boolean shouldTradeToday() {
        return dateTimeUtils.isDateValid() && dateTimeUtils.isTimeValid();
    }
}
