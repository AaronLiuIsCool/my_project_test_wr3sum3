package com.kuaidaoresume.resume.utils;

import java.time.LocalDate;
import java.util.Objects;

public class DateUtil {

    public static int getDurationInMonths(LocalDate startDate, LocalDate endDate) {
        if (Objects.isNull(endDate)) {
            endDate = LocalDate.now();
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate is later than endDate.");
        }

        int startYear = startDate.getYear();
        int startMonth = startDate.getMonthValue();

        int endYear = endDate.getYear();
        int endMonth = endDate.getMonthValue();

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
}
