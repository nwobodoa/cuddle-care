package com.ebony.cuddlecare.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DiaperUIState(
    val isWetDiaper: Boolean = false,
    val isDirtyDiaper: Boolean = false,
    val diaperCount: Int = 0
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
}