package com.ebony.cuddlecare.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZoneOffset.UTC

data class DiaperUIState(
    val isWetDiaper: Boolean = false,
    val isDirtyDiaper: Boolean = false,
    val diaperCount: Int = 0,
    val isTimeExpanded: Boolean = false,
    val showTimePicker:Boolean = false,
    val showDatePicker:Boolean = false,
    val selectedTime:LocalTime = LocalTime.now(),
    val selectedDate: LocalDate = LocalDate.now()
)


class DiaperViewModel: ViewModel() {
    private val  _diaperUIState = MutableStateFlow(DiaperUIState())
    val diaperUIState = _diaperUIState.asStateFlow()

    fun toggleWetDiaper() {
        _diaperUIState.update { it.copy(isWetDiaper = !it.isWetDiaper) }
    }

    fun toggleDirtyDiaper() {
        _diaperUIState.update { it.copy(isDirtyDiaper = !it.isDirtyDiaper) }
    }

    fun toggleDatePicker() {
        _diaperUIState.update { it.copy(isTimeExpanded = !it.isTimeExpanded) }
    }

    fun setShowTimePicker(isVisible:Boolean) {
        _diaperUIState.update { it.copy(showTimePicker = isVisible) }
    }

    fun setShowDatePicker(isVisible:Boolean) {
        _diaperUIState.update { it.copy(showDatePicker = isVisible) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setSelectedTime(localTime: LocalTime) {
        _diaperUIState.update { it.copy(selectedTime = localTime) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun epochMillisToDate(epochMillis: Long): LocalDate {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setSelectedDate(dateEpochMilli: Long) {
        _diaperUIState.update{it.copy(selectedDate =  epochMillisToDate(dateEpochMilli))}

    }
}