package com.ebony.cuddlecare.ui.viewmodel

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.documents.Document
import com.ebony.cuddlecare.ui.documents.SortableActivity
import com.ebony.cuddlecare.ui.documents.activeBabyCollection
import com.ebony.cuddlecare.util.epochMillisToDate
import com.ebony.cuddlecare.util.localDateTimeToEpoch
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

data class VaccineUIState(
    val isTimeExpanded: Boolean = false,
    val showTimePicker: Boolean = false,
    val showDatePicker: Boolean = false,
    val selectedTime: LocalTime = LocalTime.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val type: String = "",
    val notes: String = "",
    val attachments: String = "",
    val showVaccineList: Boolean = false,
    val errors: List<String> = emptyList(),
    val savedSuccessfully: Boolean = false,
    val loading:Boolean = false,
    val vaccinationRecords: List<VaccinationRecord> = emptyList()
)

data class VaccinationRecord(
    val id: String,
    val type: String,
    val notes: String,
    val attachments: String,
    val endTimeEpochSecs: Long,
    val timestamp: Long
):SortableActivity {
    override fun rank(): Long {
        return endTimeEpochSecs
    }
    constructor():this("","","","",0,0)
}

class VaccinationViewModel : ViewModel() {
    private val _vaccineUIState = MutableStateFlow((VaccineUIState()))
    val vaccineUIState = _vaccineUIState.asStateFlow()
    private val vaccinationCollection = Firebase.firestore.collection(Document.Vaccination.name)
    fun toggleDatePicker() {
        _vaccineUIState.update { it.copy(isTimeExpanded = !it.isTimeExpanded) }
    }



    fun setShowTimePicker(isVisible: Boolean) {
        _vaccineUIState.update { it.copy(showTimePicker = isVisible) }
    }

    fun setShowDatePicker(isVisible: Boolean) {
        _vaccineUIState.update { it.copy(showDatePicker = isVisible) }
    }

    fun setSelectedTime(localTime: LocalTime) {
        _vaccineUIState.update { it.copy(selectedTime = localTime) }
    }

    fun setNotes(notes: String) {
        _vaccineUIState.update { it.copy(notes = notes) }
    }

    fun validate(uiState: VaccineUIState): MutableList<String> {
        val errors = mutableListOf<String>()
        if (uiState.type.isBlank()) {
            errors.add("Select vaccination type")
        }
        return errors
    }

    private fun uiStateToRecord(uiState: VaccineUIState): VaccinationRecord {
        return VaccinationRecord(
            id = "",
            attachments = uiState.attachments,
            endTimeEpochSecs = localDateTimeToEpoch(
                LocalDateTime.of(
                    uiState.selectedDate,
                    uiState.selectedTime
                )
            )!!,
            notes = uiState.notes,
            type = uiState.type,
            timestamp = localDateTimeToEpoch(LocalDateTime.now())!!
        )

    }

    fun setSelectVaccine(s: String) {
        _vaccineUIState.update { it.copy(type = s) }
    }

    fun setShowVaccineList(b: Boolean) {
        _vaccineUIState.update { it.copy(showVaccineList = b) }
    }

    fun setSelectedDate(dateEpochMilli: Long) {
        _vaccineUIState.update { it.copy(selectedDate = epochMillisToDate(dateEpochMilli)) }
    }

    fun save(activeBaby: Baby?) {
        val uiState = _vaccineUIState.value
        val errors = validate(uiState)
        if (activeBaby == null) {
            errors.add("No active baby")

        }
        if (errors.isNotEmpty()) {
            _vaccineUIState.update { it.copy(errors = errors) }
        }

        val ref = activeBabyCollection(vaccinationCollection, activeBaby!!).document()

        val record = uiStateToRecord(uiState)
        setLoading(true)
        ref.set(record)
            .addOnCompleteListener {
                _vaccineUIState.update { it.copy(savedSuccessfully = true) }
            }.addOnFailureListener {
                Log.e(TAG, "save: ", it)
            }.addOnCompleteListener {
                setLoading(false)
            }
    }


    private fun setLoading(b: Boolean) {
        _vaccineUIState.update { it.copy(loading = b) }
    }

    fun fetchRecord(activeBaby: Baby, day: LocalDate) {
        val startOfDayEpoch = day.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
        val endOfDayEpoch = day.atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC)

        setLoading(true)
        activeBabyCollection(vaccinationCollection, activeBaby)
            .whereLessThanOrEqualTo("endTimeEpochSecs", endOfDayEpoch)
            .whereGreaterThanOrEqualTo("endTimeEpochSecs", startOfDayEpoch)
            .addSnapshotListener { snap, ex ->
                setLoading(false)
                if (ex != null) {
                    Log.e(TAG, "fetchRecord: ", ex)
                    return@addSnapshotListener
                }

                val vaccinationRecords =
                    snap?.documents?.mapNotNull { it.toObject(VaccinationRecord::class.java) }
                        ?: emptyList()
                _vaccineUIState.update { it.copy(vaccinationRecords = vaccinationRecords) }
            }
    }
}