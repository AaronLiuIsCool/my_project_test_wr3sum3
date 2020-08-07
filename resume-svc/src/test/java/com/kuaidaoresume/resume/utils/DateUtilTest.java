package com.kuaidaoresume.resume.utils;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DateUtilTest {

    @Test
    public void test_getDurationInMonths_sameYear_sameMonth() {
        Date startDate = Date.valueOf(LocalDate.of(2020, 1, 1));
        Date endDate = Date.valueOf(LocalDate.of(2020, 1, 1));
        assertThat(DateUtil.getDurationInMonths(startDate, endDate), is(0));
    }

    @Test
    public void test_getDurationInMonths_sameYear() {
        Date startDate = Date.valueOf(LocalDate.of(2020, 1, 1));
        Date endDate = Date.valueOf(LocalDate.of(2020, 2, 1));
        assertThat(DateUtil.getDurationInMonths(startDate, endDate), is(1));
    }

    @Test
    public void test_getDurationInMonths_oneYearDiff_startMonthEqualsToEndMonth() {
        Date startDate = Date.valueOf(LocalDate.of(2020, 1, 1));
        Date endDate = Date.valueOf(LocalDate.of(2021,  1, 1));
        assertThat(DateUtil.getDurationInMonths(startDate, endDate), is(12));
    }

    @Test
    public void test_getDurationInMonths_oneYearDiff_startMonthSmallerThanEndMonth() {
        Date startDate = Date.valueOf(LocalDate.of(2020, 1, 1));
        Date endDate = Date.valueOf(LocalDate.of(2021,  2, 1));
        assertThat(DateUtil.getDurationInMonths(startDate, endDate), is(13));
    }

    @Test
    public void test_getDurationInMonths_oneYearDiff_startMonthLargerThanEndMonth() {
        Date startDate = Date.valueOf(LocalDate.of(2020, 12, 1));
        Date endDate = Date.valueOf(LocalDate.of(2021,  1, 1));
        assertThat(DateUtil.getDurationInMonths(startDate, endDate), is(1));
    }

    @Test
    public void test_getDurationInMonths_moreThanOneYearsDiff_startMonthEqualsToEndMonth() {
        Date startDate = Date.valueOf(LocalDate.of(2020, 1, 1));
        Date endDate = Date.valueOf(LocalDate.of(2023,  1, 1));
        assertThat(DateUtil.getDurationInMonths(startDate, endDate), is(36));
    }

    @Test
    public void test_getDurationInMonths_moreThanOneYearsDiff_startMonthSmallerThanEndMonth() {
        Date startDate = Date.valueOf(LocalDate.of(2020, 1, 1));
        Date endDate = Date.valueOf(LocalDate.of(2023,  2, 1));
        assertThat(DateUtil.getDurationInMonths(startDate, endDate), is(37));
    }

    @Test
    public void test_getDurationInMonths_moreThanOneYearsDiff_startMonthLargerThanEndMonth() {
        Date startDate = Date.valueOf(LocalDate.of(2020, 12, 1));
        Date endDate = Date.valueOf(LocalDate.of(2023,  1, 1));
        assertThat(DateUtil.getDurationInMonths(startDate, endDate), is(25));
    }
}