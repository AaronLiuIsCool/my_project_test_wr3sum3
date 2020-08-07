package com.kuaidaoresume.resume.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static int getDurationInMonths(Date startDate, Date endDate) {
        if (startDate.getTime() > endDate.getTime()) {
            throw new IllegalArgumentException("startDate is later than endDate.");
        }

        Calendar startCal = getCalendar(startDate);
        Calendar endCal = getCalendar(endDate);

        int startYear = startCal.get(Calendar.YEAR);
        int startMonth = startCal.get(Calendar.MONTH);

        int endYear = endCal.get(Calendar.YEAR);
        int endMonth = endCal.get(Calendar.MONTH);

        int yearsDiff = endYear - startYear;
        int monthsDuration = 0;
        if (yearsDiff  > 0) {
            monthsDuration += 12 * (yearsDiff - 1);
            if (endMonth < startMonth) {
                monthsDuration += 12 - startMonth + endMonth;
            } else {
                monthsDuration += 12 + endMonth - startMonth;
            }
        } else {
            monthsDuration = endMonth - startMonth;
        }
        return monthsDuration;
    }

    private static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
