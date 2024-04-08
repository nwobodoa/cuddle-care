package com.ebony.cuddlecare.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.ebony.cuddlecare.util.epochMillisToDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

data class StatsUIState(
    val startDate:LocalDate = LocalDate.now().minusDays(1),
    val endDate:LocalDate = LocalDate.now(),
    val endDateExpanded:Boolean = false,
    val startDateExpanded:Boolean = false,
)
class StatsViewModel:ViewModel() {
   private val _statsUIState = MutableStateFlow(StatsUIState())
    val statsUIState = _statsUIState.asStateFlow()

    fun setStartDate(date:Long) {
        _statsUIState.update { it.copy(startDate = epochMillisToDate(date)) }
    }

    fun setEndDate(date:Long) {
        _statsUIState.update { it.copy(endDate = epochMillisToDate(date)) }
    }
    fun toggleStartDateExpanded() {
        _statsUIState.update { it.copy(startDateExpanded = !it.startDateExpanded) }
    }
    fun toggleEndDateExpanded() {
        _statsUIState.update { it.copy(endDateExpanded = !it.endDateExpanded) }
    }
}