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
import java.time.ZoneOffset

data class VaccineUIState(
    val isTimeExpanded: Boolean = false,
    val showTimePicker: Boolean = false,
    val showDatePicker: Boolean = false,
    val selectedTime: LocalTime = LocalTime.now(),
    val selectedDate: LocalDate = LocalDate.now()
)
class VaccinationViewModel: ViewModel(){
    private val _vaccineUIState = MutableStateFlow((VaccineUIState()))
    val vaccineUIState = _vaccineUIState.asStateFlow()
    fun toggleDatePicker() {
        _vaccineUIState.update { it.copy(isTimeExpanded = !it.isTimeExpanded) }
    }

    fun setShowTimePicker(isVisible:Boolean) {
        _vaccineUIState.update { it.copy(showTimePicker = isVisible) }
    }

    fun setShowDatePicker(isVisible:Boolean) {
        _vaccineUIState.update { it.copy(showDatePicker = isVisible) }
    }

    fun setSelectedTime(localTime: LocalTime) {
        _vaccineUIState.update { it.copy(selectedTime = localTime) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun epochMillisToDate(epochMillis: Long): LocalDate {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setSelectedDate(dateEpochMilli: Long) {
        _vaccineUIState.update{it.copy(selectedDate =  epochMillisToDate(dateEpochMilli))}

    }
}