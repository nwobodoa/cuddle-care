package com.ebony.cuddlecare.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class ReminderType {
    BASIC,
    ADVANCED
}

data class BasicReminder(
    val timeToRepeatSeconds: Long = 0,
    val isDoNotDisturb: Boolean = false
)

data class AdvancedReminder(
    val localDateEpoch:Long = 0,
    val isRepeated: Boolean = false,
    val timeSeconds: Long = 54000

)

data class ReminderUIState(
    val reminderSubject:String? = null,
    val reminderType: ReminderType = ReminderType.BASIC,
    val basicReminder: BasicReminder = BasicReminder(),
    val advancedReminder: AdvancedReminder = AdvancedReminder()

)
class ReminderViewModel:ViewModel() {
    private val _reminderUIState: MutableStateFlow<ReminderUIState> =
        MutableStateFlow(ReminderUIState())

    val reminderUIState: StateFlow<ReminderUIState> = _reminderUIState.asStateFlow()

    fun setSelectedReminderSubject(type: String) {
        _reminderUIState.update { it.copy(reminderSubject = type) }
    }

    fun setSelectedReminderType(type: ReminderType) {
        _reminderUIState.update { it.copy(reminderType = type) }
    }

    fun setDoNotDisturb(state: Boolean) {
        _reminderUIState.update { it.copy(basicReminder = it.basicReminder.copy(isDoNotDisturb = state)) }
    }

    fun setReminderRepeated(isRepeated: Boolean) {
        _reminderUIState.update { it.copy(advancedReminder = it.advancedReminder.copy(isRepeated = isRepeated)) }
    }

    fun setDate(date: Long) {
        _reminderUIState.update { it.copy(advancedReminder = it.advancedReminder.copy(localDateEpoch = date)) }
    }

    fun setAdvancedReminderTime(timeSeconds: Long) {

        //TODO validate that time is between 0 and (11*60*60) + (59 * 60); it between 12am and 11:59pm
        _reminderUIState.update { it.copy(advancedReminder = it.advancedReminder.copy(timeSeconds = timeSeconds)) }
    }
}