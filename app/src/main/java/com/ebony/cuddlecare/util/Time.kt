package com.ebony.cuddlecare.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

fun secondsToFormattedTime(ticks: Long): String {
    val hours = TimeUnit.SECONDS.toHours(ticks)
    val minutes = TimeUnit.SECONDS.toMinutes(ticks) % 60
    val seconds = TimeUnit.SECONDS.toSeconds(ticks) % 60
    val hoursPart = if (hours > 0) "$hours h " else ""
    val minutesPart = if (minutes > 0) "$minutes m " else ""

    if (hoursPart.isNotBlank()) {
        return "$hoursPart$minutesPart"
    }

    return "$hoursPart$minutesPart$seconds s"
}

fun localDateTimeToDate(localDateTime: LocalDateTime?): String {
    if (localDateTime == null) {
        return "_ __"
    }

    return localDateTime.format(
        DateTimeFormatter.ofPattern(
            "d MMM",
            Locale.ENGLISH
        )
    )
}

fun localDateTimeToEpoch(localDateTime: LocalDateTime?): Long? {
    if (localDateTime == null) return null
    return localDateTime.toEpochSecond(ZoneOffset.UTC)
}


fun localDateTimeToTime(localDateTime: LocalDateTime?): String {
    if (localDateTime == null) {
        return "__:__ __"
    }
    return localDateTime.format(
        DateTimeFormatter.ofPattern(
            "h:mm a",
            Locale.ENGLISH
        )
    )
}

fun localTimeToEpochUTC(localTime: LocalTime): Long {
    val today = LocalDate.now()
    val localDateTime = LocalDateTime.of(today, localTime)
    val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneOffset.UTC)
    return zonedDateTime.toEpochSecond()
}


fun epochMillisToDate(epochMillis: Long): LocalDate {
    return Instant.ofEpochMilli(epochMillis)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
}

fun epochSecondsToLocalDateTime(epochSeconds: Long): LocalDateTime? {
    return Instant.ofEpochSecond(epochSeconds)
        .atZone(ZoneOffset.UTC)
        .toLocalDateTime()
}

fun timestampToString(timestamp:Long?): String {
    if(timestamp == null) return  ""
    return epochSecondsToLocalDateTime(timestamp)?.format(DateTimeFormatter.ofPattern("h:mm a"))
        ?: ""
}