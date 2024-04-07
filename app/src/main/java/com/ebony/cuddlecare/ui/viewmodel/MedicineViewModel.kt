package com.ebony.cuddlecare.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.ebony.cuddlecare.util.epochMillisToDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime

val unitLists = listOf("g", "mg", "oz", "ml", "fl oz", "drops", "pcs", "tsp", "tbsp")

data class MedicineUIState(
    val units: List<String> = unitLists,
    val selectedUnit: String = unitLists.first(),
    val isTimeExpanded: Boolean = false,
    val showTimePicker: Boolean = false,
    val showDatePicker: Boolean = false,
    val selectedTime: LocalTime = LocalTime.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val isUnitDropdownExpanded: Boolean = false,
    val qty: Long = 0L
)

class MedicineViewModel : ViewModel() {
    private val _medicineUIState = MutableStateFlow(MedicineUIState())
    val medicineUIState = _medicineUIState.asStateFlow()

    fun toggleDatePicker() {
        _medicineUIState.update { it.copy(isTimeExpanded = !it.isTimeExpanded) }
    }

    fun setShowTimePicker(isVisible: Boolean) {
        _medicineUIState.update { it.copy(showTimePicker = isVisible) }
    }

    fun setShowDatePicker(isVisible: Boolean) {
        _medicineUIState.update { it.copy(showDatePicker = isVisible) }
    }

    fun setSelectedTime(localTime: LocalTime) {
        _medicineUIState.update { it.copy(selectedTime = localTime) }
    }

    fun setSelectedDate(dateEpochMilli: Long) {
        _medicineUIState.update { it.copy(selectedDate = epochMillisToDate(dateEpochMilli)) }
    }

    fun setIsUnitDropdownExpanded(state: Boolean) {
        _medicineUIState.update { it.copy(isUnitDropdownExpanded = state) }
    }

    fun setSelectedUnit(unit: String) {
        _medicineUIState.update { it.copy(selectedUnit = unit) }
    }
}