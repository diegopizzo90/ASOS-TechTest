package com.diegopizzo.asostechtest.util

import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class UtilsTest {
    @Test
    fun convertUtcDateTimeToLocalTime_dateConverted_assertEqualsTrue() {
        val date = Utils.convertUtcDateTimeToLocalTime(
            "2006-03-24T22:30:00.000Z",
            ZoneId.systemDefault(),
            TIME_PATTERN
        )
        assertEquals("22:30", date)
    }

    @Test
    fun convertUtcDateTimeToLocalDate_dateConverted_assertEqualsTrue() {
        val date = Utils.convertUtcDateTimeToLocalDate(
            "2006-03-24T22:30:00.000Z",
            ZoneId.systemDefault(),
            DATE_PATTERN
        )
        assertEquals("24/03/2006", date)
    }

    @Test
    fun getRemainingDays_daysLeft_assertEqualsTrue() {
        val days = Utils.getRemainingDays(
            startDateUtc = ZonedDateTime.of(2021, 12, 17, 16, 6, 0, 0, ZoneId.systemDefault()),
            "2018-04-02T23:51:00.000Z"
        )
        assertEquals(1354, days)
    }

    @Test
    fun fromDateStringToZonedDate_zonedDate_assertEqualsTrue() {
        val zonedDate = Utils.fromDateStringToZonedDateTimeUtc("2018-04-02T23:51:00.000Z")
        assertEquals(ZonedDateTime.of(2018, 4, 2, 23, 51, 0, 0, ZoneOffset.UTC), zonedDate)
    }

    @Test
    fun isDateUtcGreaterThan_isGreater_assertEqualsTrue() {
        val isGreater = Utils.isDateUtcGreaterEqualsThan(2019, "2019-04-02T23:51:00.000Z")
        assertEquals(true, isGreater)
    }

    @Test
    fun isDateUtcGreaterThan_isEquals_assertEqualsTrue() {
        val isEquals = Utils.isDateUtcLowerEqualsThan(2019, "2019-01-01T00:00:00.000Z")
        assertEquals(true, isEquals)
    }

    @Test
    fun isDateUtcLowerThan_isLower_assertEqualsTrue() {
        val isLower = Utils.isDateUtcLowerEqualsThan(2019, "2018-04-02T23:51:00.000Z")
        assertEquals(true, isLower)
    }

    @Test
    fun isDateUtcLowerThan_isLower_assertEqualsFalse() {
        val isLower = Utils.isDateUtcLowerEqualsThan(2019, "2020-12-31T23:51:00.000Z")
        assertEquals(false, isLower)
    }

    @Test
    fun isDateUtcLowerThan_isEquals_assertEqualsTrue() {
        val isEquals = Utils.isDateUtcLowerEqualsThan(2019, "2019-12-31T23:59:58.000Z")
        assertEquals(true, isEquals)
    }

    companion object {
        const val DATE_PATTERN = "dd/MM/yyyy"
        const val TIME_PATTERN = "HH:mm"
    }
}