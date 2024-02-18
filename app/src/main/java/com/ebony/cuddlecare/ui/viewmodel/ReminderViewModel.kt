package com.ebony.cuddlecare.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ReminderUIState(val selectedReminderType:String? = null)
class ReminderViewModel:ViewModel() {
    private val  _reminderUIState:MutableStateFlow<ReminderUIState> = MutableStateFlow(ReminderUIState())

    val reminderUIState:StateFlow<ReminderUIState> = _reminderUIState.asStateFlow()

    fun setSelectedReminderType(type:String) {
        _reminderUIState.update { it.copy(selectedReminderType  = type) }
    }

}