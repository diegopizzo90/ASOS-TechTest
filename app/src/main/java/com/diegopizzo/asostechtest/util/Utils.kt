package com.diegopizzo.asostechtest.util

import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

object Utils {

    fun convertUtcDateTimeToLocalDate(
        utcDate: String,
        timeZone: ZoneId = ZoneId.systemDefault(),
        datePattern: String
    ): String {
        return ZonedDateTime.parse(utcDate, DateTimeFormatter.ISO_DATE_TIME)
            .withZoneSameInstant(timeZone)
            .format(
                DateTimeFormatter.ofPattern(datePattern)
            )
    }

    fun convertUtcDateTimeToLocalTime(
        utcDate: String,
        timeZone: ZoneId = ZoneId.systemDefault(),
        timePattern: String
    ): String {
        return ZonedDateTime.parse(utcDate, DateTimeFormatter.ISO_DATE_TIME)
            .withZoneSameInstant(timeZone)
            .format(
                DateTimeFormatter.ofPattern(timePattern)
            )
    }

    fun getRemainingDays(
        startDateUtc: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
        endDateUtc: String
    ): Long {
        val endDate = ZonedDateTime.parse(endDateUtc, DateTimeFormatter.ISO_DATE_TIME)
            .withZoneSameInstant(ZoneId.systemDefault())
        return ChronoUnit.DAYS.between(endDate, startDateUtc)
    }

    fun fromDateStringToZonedDateTimeUtc(dateUtc: String): ZonedDateTime {
        return ZonedDateTime.parse(dateUtc, DateTimeFormatter.ISO_DATE_TIME)
            .withZoneSameInstant(ZoneOffset.UTC)
    }

    fun getYearFromStringDate(dateUtc: String): Int {
        return fromDateStringToZonedDateTimeUtc(dateUtc).year
    }

    fun getActualYear(): Int {
        return ZonedDateTime.now(ZoneId.systemDefault()).year
    }

    fun isDateUtcGreaterEqualsThan(fromYear: Int, dateUtcString: String): Boolean {
        val fromDate = ZonedDateTime.of(fromYear, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        val dateUtc = fromDateStringToZonedDateTimeUtc(dateUtcString)
        return dateUtc.isAfter(fromDate).or(dateUtc.isEqual(fromDate))
    }

    fun isDateUtcLowerEqualsThan(toYear: Int, dateUtcString: String): Boolean {
        val fromDate = ZonedDateTime.of(toYear, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC)
        val dateUtc = fromDateStringToZonedDateTimeUtc(dateUtcString)
        return dateUtc.isBefore(fromDate).or(dateUtc.isEqual(fromDate))
    }
}