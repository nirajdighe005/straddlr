package com.straddler.services;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

@Component
public class DateTimeUtils {

    public Date getNextThursday() {
        LocalDate ld = LocalDate.now();
        LocalDate nextThursday = ld.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
        if (Constants.HOLIDAYS.contains(nextThursday)) {
            nextThursday = ld.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
        }
        return Date.from(nextThursday.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public boolean isTimeValid() {
        LocalTime start = LocalTime.parse( "09:15:00" );
        LocalTime stop = LocalTime.parse( "15:30:00" );
        LocalTime now = LocalTime.now();
        return now.isAfter(start) && now.isBefore(stop);
    }

    public boolean isDateValid() {
        LocalDate now = LocalDate.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        return !List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(dayOfWeek);
    }
}
