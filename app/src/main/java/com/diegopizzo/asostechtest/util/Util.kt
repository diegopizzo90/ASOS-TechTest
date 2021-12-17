package com.diegopizzo.asostechtest.util

import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

object Util {

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
}