package com.ebony.cuddlecare.ui.components

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

fun epochMillisToDate(epochMillis: Long): LocalDate {
    return Instant.ofEpochMilli(epochMillis)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
}