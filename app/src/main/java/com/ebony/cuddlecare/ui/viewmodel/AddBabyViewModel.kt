package com.ebony.cuddlecare.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.ebony.cuddlecare.ui.components.epochMillisToDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

data class AddBabyUIState(
    val selectedGender: String = "Boy",
    val babyName:String = "David",
    val isPremature:Boolean = false,
    val selectedDate: LocalDate = LocalDate.now(),
    val showDatePicker:Boolean = false,
    val isDateExpanded: Boolean = false

    )

class AddBabyViewModel : ViewModel() {
    private val _addBabyUIState: MutableStateFlow<AddBabyUIState> =
        MutableStateFlow(AddBabyUIState())
    val addBabyUIState: StateFlow<AddBabyUIState> = _addBabyUIState.asStateFlow()

    fun setSelectedGender(gender: String) {
       // _addBabyUIState.update{it.copy(seleetedGender = gender)}
    }

    fun showDatePicker(isVisible: Boolean){
        _addBabyUIState.update { it.copy(showDatePicker = isVisible) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setSelectedDate(l: Long) {
        _addBabyUIState.update { it.copy(selectedDate = epochMillisToDate(l)) }
    }

    fun toggableDatePicker(){
        _addBabyUIState.update { it.copy(isDateExpanded = !it.isDateExpanded) }
    }
}