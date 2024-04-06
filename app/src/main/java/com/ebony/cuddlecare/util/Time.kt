package com.ebony.cuddlecare.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

fun secondsToFormattedString(ticks: Long): String {
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

fun localDateTimeToDate(epochSeconds: Long?): String {
    if (epochSeconds == null) {
        return "_ __"
    }

    val localDateTime =
        LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZoneId.systemDefault())

    return localDateTime.format(
        DateTimeFormatter.ofPattern(
            "d MMM",
            Locale.ENGLISH
        )
    )
}

fun localDateTimeToTime(epochSeconds: Long?): String {
    if (epochSeconds == null) {
        return "__:__ __"
    }

    val localDateTime =
        LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZoneId.systemDefault())
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
