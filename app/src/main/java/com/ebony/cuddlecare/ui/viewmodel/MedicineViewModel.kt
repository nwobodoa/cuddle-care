package com.ebony.cuddlecare.ui.viewmodel

import android.content.ContentValues
import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

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
    val dosage: Int = 0,
    val errors: List<String> = emptyList(),
    val notes: String = "",
    val savedSuccessfully: Boolean = false,
    val medicineRecords: List<MedicineRecord> = emptyList(),
    val loading: Boolean = false
)

data class MedicineRecord(
    val id: String,
    val unit: String,
    val endTimestampEpocSecs: Long,
    val timestamp: Long = localDateTimeToEpoch(LocalDateTime.now())!!,
    val notes: String
) : SortableActivity {
    override fun rank(): Long {
        return timestamp
    }
}

class MedicineViewModel : ViewModel() {
    private val _medicineUIState = MutableStateFlow(MedicineUIState())
    val medicineUIState = _medicineUIState.asStateFlow()

    private val medicineCollection = Firebase.firestore.collection(Document.Medicine.name)


    fun setLoading(loading: Boolean) {
        _medicineUIState.update { it.copy(loading = loading) }
    }

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

    fun setDosage(dosage: Int) {
        _medicineUIState.update { it.copy(dosage = dosage) }
    }

    fun setNotes(notes: String) {
        _medicineUIState.update { it.copy(notes = notes) }
    }

    private fun uiStateToRecord(medicineUIState: MedicineUIState): MedicineRecord {
        return MedicineRecord(
            id = "",
            unit = medicineUIState.selectedUnit,
            endTimestampEpocSecs = localDateTimeToEpoch(
                LocalDateTime.of(
                    medicineUIState.selectedDate,
                    medicineUIState.selectedTime
                )
            )!!,
            notes = medicineUIState.notes
        )
    }

    private fun validation(medicineUIState: MedicineUIState): MutableList<String> {
        val errors = mutableListOf<String>()

        if (medicineUIState.dosage <= 0) {
            errors.add("Dosage must be greater than zero")
        }
        return errors
    }

    fun save(activeBaby: Baby?) {
        val uiState = _medicineUIState.value
        val errors = validation(uiState)
        if (activeBaby == null) {
            errors.add("No active baby")
        }
        if (errors.isNotEmpty()) {
            _medicineUIState.update { it.copy(errors = errors) }
            return
        }
        val ref = activeBabyCollection(medicineCollection, activeBaby!!).document()
        val record = uiStateToRecord(uiState).copy(id = ref.id)
        ref.set(record)
            .addOnSuccessListener {
                _medicineUIState.update { it.copy(savedSuccessfully = true) }
            }

    }

    fun fetchRecord(activeBaby: Baby, day: LocalDate) {
        val startOfDayEpoch = day.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
        val endOfDayEpoch = day.atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC)

        setLoading(true)
        activeBabyCollection(medicineCollection, activeBaby)
            .whereLessThanOrEqualTo("endTimeEpochSecs", endOfDayEpoch)
            .whereGreaterThanOrEqualTo("endTimeEpochSecs", startOfDayEpoch)
            .addSnapshotListener { snap, ex ->
                setLoading(false)
                if (ex != null) {
                    Log.e(ContentValues.TAG, "fetchRecord: ", ex)
                    return@addSnapshotListener
                }

                val medicineRecords =
                    snap?.documents?.mapNotNull { it.toObject(MedicineRecord::class.java) }
                        ?: emptyList()
                _medicineUIState.update { it.copy(medicineRecords = medicineRecords) }

            }
    }

}