package com.diegopizzo.asostechtest.util

import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

class UtilTest {
    @Test
    fun convertUtcDateTimeToLocalTime_dateConverted_assertEqualsTrue() {
        val date = Util.convertUtcDateTimeToLocalTime(
            "2006-03-24T22:30:00.000Z",
            ZoneId.systemDefault(),
            TIME_PATTERN
        )
        assertEquals("22:30", date)
    }

    @Test
    fun convertUtcDateTimeToLocalDate_dateConverted_assertEqualsTrue() {
        val date = Util.convertUtcDateTimeToLocalDate(
            "2006-03-24T22:30:00.000Z",
            ZoneId.systemDefault(),
            DATE_PATTERN
        )
        assertEquals("24/03/2006", date)
    }

    @Test
    fun getRemainingDays_daysLeft_assertEqualsTrue() {
        val days = Util.getRemainingDays(
            startDateUtc = ZonedDateTime.of(2021, 12, 17, 16, 6, 0, 0, ZoneId.systemDefault()),
            "2018-04-02T23:51:00.000Z"
        )
        assertEquals(1354, days)
    }

    companion object {
        const val DATE_PATTERN = "dd/MM/yyyy"
        const val TIME_PATTERN = "HH:mm"
    }
}