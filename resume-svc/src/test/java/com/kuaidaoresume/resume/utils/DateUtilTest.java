package com.kuaidaoresume.resume.utils;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DateUtilTest {

    @Test
    public void test_getDurationInMonths_sameYear_sameMonth() {
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 1, 1);
        assertThat(DateUtil.getDurationInMonths(startDate, endDate), is(0));
    }

    @Test
    public void test_getDurationInMonths_sameYear() {
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 2, 1);
        assertThat(DateUtil.getDurationInMonths(startDate, endDate), is(1));
    }

    @Test
    public void test_getDurationInMonths_oneYearDiff_startMonthEqualsToEndMonth() {
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2021,  1, 1);
        assertThat(DateUtil.getDurationInMonths(startDate, endDate), is(12));
    }

    @Test
    public void test_getDurationInMonths_oneYearDiff_startMonthSmallerThanEndMonth() {
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2021,  2, 1);
        assertThat(DateUtil.getDurationInMonths(startDate, endDate), is(13));
    }

    @Test
    public void test_getDurationInMonths_oneYearDiff_startMonthLargerThanEndMonth() {
        LocalDate startDate = LocalDate.of(2020, 12, 1);
        LocalDate endDate = LocalDate.of(2021,  1, 1);
        assertThat(DateUtil.getDurationInMonths(startDate, endDate), is(1));
    }

    @Test
    public void test_getDurationInMonths_moreThanOneYearsDiff_startMonthEqualsToEndMonth() {
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2023,  1, 1);
        assertThat(DateUtil.getDurationInMonths(startDate, endDate), is(36));
    }

    @Test
    public void test_getDurationInMonths_moreThanOneYearsDiff_startMonthSmallerThanEndMonth() {
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2023,  2, 1);
        assertThat(DateUtil.getDurationInMonths(startDate, endDate), is(37));
    }

    @Test
    public void test_getDurationInMonths_moreThanOneYearsDiff_startMonthLargerThanEndMonth() {
        LocalDate startDate = LocalDate.of(2020, 12, 1);
        LocalDate endDate = LocalDate.of(2023,  1, 1);
        assertThat(DateUtil.getDurationInMonths(startDate, endDate), is(25));
    }
}
