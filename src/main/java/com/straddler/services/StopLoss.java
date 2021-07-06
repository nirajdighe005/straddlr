package com.straddler.services;

import java.time.DayOfWeek;

public enum StopLoss {
    Monday(1.3, DayOfWeek.MONDAY), Tuesday(1.3, DayOfWeek.TUESDAY), Wednesday(1.4, DayOfWeek.WEDNESDAY),
    Thursday(1.7, DayOfWeek.THURSDAY), Friday(1.3, DayOfWeek.FRIDAY);

    StopLoss(double v, DayOfWeek monday) {

    }
}
