package com.straddler.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonConstants {
    public static final Map<DayOfWeek, Double> STOP_LOSS = Map.of(DayOfWeek.MONDAY, 0.7, DayOfWeek.TUESDAY, 1.15,
            DayOfWeek.WEDNESDAY, 1.2, DayOfWeek.THURSDAY, 1.35, DayOfWeek.FRIDAY, 1.15);
    public static final List<LocalDate> HOLIDAYS = Collections.singletonList(LocalDate.of(2021, 8, 19));

    public static final String ORDER_STATUS_COMPLETE = "COMPLETE";
    public static final String ORDER_STATUS_REJECTED = "REJECTED";
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";
    public static final String ORDER_STATUS_OPEN = "OPEN";
}
