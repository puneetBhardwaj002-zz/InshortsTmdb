package com.example.inshortsmovie.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class AppUtilsTest {

    @Test
    public void isNullOrEmptyString_nullString_returnTrue() {
        String input = null;
        assertTrue(AppUtils.isNullOrEmptyString(input));
    }

    @Test
    public void isNullOrEmptyString_emptyString_returnTrue() {
        String input = "";
        assertTrue(AppUtils.isNullOrEmptyString(input));
    }

    @Test
    public void isNullOrEmptyString_nonNullNonEmptyString_returnFalse() {
        String input = "testString";
        assertFalse(AppUtils.isNullOrEmptyString(input));
    }

    @Test
    public void parseDouble_validDouble_returnStringAmount() {
        assertEquals(AppUtils.parseDouble(-76.78273),"-76.78273");
    }

    @Test
    public void parseRuntime_zeroMinute_returnEmpty() {
        int time = 0;
        assertEquals(AppUtils.parseRuntime(time),"");
    }

    @Test
    public void parseRuntime_oneMinute_returnMinute() {
        int time = 1;
        assertEquals(AppUtils.parseRuntime(time),"1 minute");
    }

    @Test
    public void parseRuntime_timeLessThanHour_returnMinutes() {
        int time = 55;
        assertEquals(AppUtils.parseRuntime(time),"55 minutes");
    }

    @Test
    public void parseRuntime_sixtyMinutes_returnHour() {
        int time = 60;
        assertEquals(AppUtils.parseRuntime(time),"1 hour");
    }

    @Test
    public void parseRuntime_exactHours_returnHours() {
        int time = 120;
        assertEquals(AppUtils.parseRuntime(time),"2 hours");
    }

    @Test
    public void parseRuntime_oneMinuteNonZeroHour_returnHoursAndMinute() {
        int time = 121;
        assertEquals(AppUtils.parseRuntime(time),"2 hours 1 minute");
    }

    @Test
    public void parseRuntime_fiveMinuteNonZeroHour_returnHoursAndMinutes() {
        int time = 128;
        assertEquals(AppUtils.parseRuntime(time),"2 hours 8 minutes");
    }

    @Test
    public void parseAmount_validAmountTillHundreds_returnNoCommaSeparation() {
        long amount = 300;
        assertEquals(AppUtils.parseAmount(amount),"300");
    }

    @Test
    public void parseAmount_validAmountTillHundredThousand_returnSingleCommaSeparation() {
        long amount = 300000;
        assertEquals(AppUtils.parseAmount(amount),"300,000");
    }

    @Test
    public void parseAmount_validAmountTillMillion_returnTwoCommaSeparation() {
        long amount = 3000000;
        assertEquals(AppUtils.parseAmount(amount),"3,000,000");
    }
}