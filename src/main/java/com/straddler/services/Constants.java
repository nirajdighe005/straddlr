package com.straddler.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {
    public static final Map<DayOfWeek, Double> STOP_LOSS = Map.of(DayOfWeek.MONDAY, 1.3, DayOfWeek.TUESDAY, 1.3,
            DayOfWeek.WEDNESDAY, 1.4, DayOfWeek.THURSDAY, 1.7, DayOfWeek.FRIDAY, 1.3);
    public static final List<LocalDate> HOLIDAYS = Collections.singletonList(LocalDate.of(2021, 8, 19));

}
