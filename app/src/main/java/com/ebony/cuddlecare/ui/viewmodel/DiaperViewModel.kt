package com.ebony.cuddlecare.ui.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.documents.DiaperCount
import com.ebony.cuddlecare.ui.documents.DiaperRecord
import com.ebony.cuddlecare.ui.documents.DiaperSoilType
import com.ebony.cuddlecare.ui.documents.DiaperType
import com.ebony.cuddlecare.ui.documents.Document
import com.ebony.cuddlecare.util.epochMillisToDate
import com.ebony.cuddlecare.util.localDateTimeToEpoch
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import kotlin.math.max

data class DiaperUIState(
    val babyId: String? = null,
    val isTimeExpanded: Boolean = false,
    val showTimePicker: Boolean = false,
    val showDatePicker: Boolean = false,
    val selectedTime: LocalTime = LocalTime.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val loading: Boolean = false,
    val diaperCount: DiaperCountUI? = null,
    val showDiaperRefill: Boolean = false,
    val showDiaperWarning: Boolean = true,
    val diaperType: DiaperType? = null,
    val showDiaperTypeDropdown: Boolean = false,
    val notes: String = "",
    val diaperSoilState: Set<DiaperSoilType> = emptySet(),
    val errors: Set<String> = emptySet(),
    val attachmentURL: String = "",
    val savedSuccessfully: Boolean = false
)



data class DiaperCountUI(
    val babyId: String,
    val count: String,
    val lastRefillEpoch: Long
)

class DiaperViewModel : ViewModel() {
    private val _diaperUIState = MutableStateFlow(DiaperUIState())
    val diaperUIState = _diaperUIState.asStateFlow()
    private val db = Firebase.firestore
    private val diaperCountCol = db.collection(Document.DiaperCount.name)
    private val diaperCol = db.collection(Document.Diaper.name)

    private fun addSoilType(diaperSoilType: DiaperSoilType) {
        _diaperUIState.update { it.copy(diaperSoilState = it.diaperSoilState + diaperSoilType) }
    }

    fun setNotes(notes: String) {
        _diaperUIState.update { it.copy(notes = notes) }
    }

    private fun removeSoilType(diaperSoilType: DiaperSoilType) {
        _diaperUIState.update { it.copy(diaperSoilState = it.diaperSoilState - diaperSoilType) }
    }

    fun toggleWetDiaper() {
        val isWet = _diaperUIState.value.diaperSoilState.contains(DiaperSoilType.WET)
        if (isWet) removeSoilType(DiaperSoilType.WET) else addSoilType(DiaperSoilType.WET)
    }

    fun toggleDirtyDiaper() {
        val isDirty = _diaperUIState.value.diaperSoilState.contains(DiaperSoilType.DIRTY)
        if (isDirty) removeSoilType(DiaperSoilType.DIRTY) else addSoilType(DiaperSoilType.DIRTY)
    }

    fun toggleDatePicker() {
        _diaperUIState.update { it.copy(isTimeExpanded = !it.isTimeExpanded) }
    }

    fun setShowTimePicker(isVisible: Boolean) {
        _diaperUIState.update { it.copy(showTimePicker = isVisible) }
    }

    fun setShowDatePicker(isVisible: Boolean) {
        _diaperUIState.update { it.copy(showDatePicker = isVisible) }
    }

    fun setSelectedTime(localTime: LocalTime) {
        _diaperUIState.update { it.copy(selectedTime = localTime) }
    }

    fun setSelectedDate(dateEpochMilli: Long) {
        _diaperUIState.update { it.copy(selectedDate = epochMillisToDate(dateEpochMilli)) }
    }

    fun setShowDiaperRefill(state: Boolean) {
        _diaperUIState.update { it.copy(showDiaperRefill = state) }
    }

    fun setShowDiaperWarning(state: Boolean) {
        _diaperUIState.update { it.copy(showDiaperWarning = state) }
    }

    fun fetchDiaperCount(activeBaby: Baby?) {
        if (activeBaby == null) {
            _diaperUIState.update { it.copy(loading = false) }
            return
        }
        diaperCountCol.document(activeBaby.id)
            .addSnapshotListener { snapshot, ex ->
                if (ex != null) {
                    Log.e(TAG, "fetchDiaper: error fetching diaper count", ex)
                    return@addSnapshotListener
                }
                val diaperCount = snapshot?.toObject(DiaperCount::class.java)
                if (diaperCount != null) {
                    Log.i(TAG, "fetchDiaper: loaded diaperCount")
                    _diaperUIState.update {
                        it.copy(
                            diaperCount = DiaperCountUI(
                                babyId = diaperCount.babyId,
                                count = diaperCount.count.toString(),
                                lastRefillEpoch = diaperCount.lastRefillEpoch
                            )
                        )
                    }
                }

            }
    }

    fun setDiaperCountWarning(state: Boolean) {
        _diaperUIState.update {
            it.copy(
                showDiaperWarning = state
            )
        }
    }

    //TODO what about increasing it?
    fun setDiaperCount(activeBaby: Baby?, count: String) {
        if (activeBaby == null) {
            Log.i(TAG, "setDiaperCount: No active baby")
            return
        }
        _diaperUIState.update {
            it.copy(
                diaperCount = DiaperCountUI(
                    babyId = activeBaby.id,
                    count = count,
                    lastRefillEpoch = localDateTimeToEpoch(
                        LocalDateTime.now()
                    )!!
                )
            )
        }
    }

    private fun diaperCountUiToRecord(diaperCountUI: DiaperCountUI?): DiaperCount? {
        return diaperCountUI?.let {
            DiaperCount(
                babyId = diaperCountUI.babyId,
                count = it.count.toLongOrNull() ?: 0,
                lastRefillEpoch = diaperCountUI.lastRefillEpoch
            )
        }
    }

    private fun setLoading(loading: Boolean) {
        _diaperUIState.update { it.copy(loading = loading) }
    }

    fun saveDiaperCount() {
        val diaperCount = _diaperUIState.value.diaperCount
        if (diaperCount?.babyId == null) {
            setShowDiaperRefill(false)
            //TODO handle no baby
            return
        }
        diaperCountUiToRecord(diaperCount)?.let {
            setLoading(true)
            diaperCountCol.document(it.babyId).set(it)
                .addOnCompleteListener {
                    setLoading(false)
                    setShowDiaperRefill(false)
                }
        }

    }

    fun setSelectedDiaperType(diaperType: DiaperType) {
        _diaperUIState.update { it.copy(diaperType = diaperType) }
    }

    fun setShowDiaperTypeDropdown(state: Boolean) {
        _diaperUIState.update { it.copy(showDiaperTypeDropdown = state) }
    }

    private fun validate(diaperUIState: DiaperUIState): Set<String> {
        val errors = mutableSetOf<String>()
        if (diaperUIState.diaperType == null) {
            errors.add("Please Select Diaper Type")
        }
        if (diaperUIState.babyId == null) {
            errors.add("No active baby selected")
        }
        return errors
    }


    fun save() {
        val diaperUIState = _diaperUIState.value
        val errors = validate(diaperUIState)
        if (errors.isNotEmpty()) {
            _diaperUIState.update { it.copy(errors = errors) }
            return
        }
        val record = diaperUIToDiaperRecord(diaperUIState)
        setLoading(true)
        db.runTransaction { tx ->
            val diaperRef = diaperCol.document(diaperUIState.babyId!!)
            val diaperCountRef = diaperCountCol.document(diaperUIState.babyId!!)
            val updateCount =
                tx.get(diaperCountRef)
                    .toObject(DiaperCount::class.java)?.let {
                        it.copy(count = max(it.count - 1, 0))
                    }

            if (updateCount != null) {
                tx.set(diaperRef, record)
                tx.set(diaperCountRef, updateCount)
            } else {
                throw FirebaseFirestoreException(
                    "No Diaper Count Record",
                    FirebaseFirestoreException.Code.ABORTED,
                )
            }
        }.addOnSuccessListener {
            _diaperUIState.update { it.copy(savedSuccessfully = true) }
        }.addOnCompleteListener {
            setLoading(false)
        }.addOnFailureListener {
            Log.e(TAG, "save: ", it)
        }
    }

    fun setActiveBaby(activeBaby: Baby?) {
        _diaperUIState.update { it.copy(babyId = activeBaby?.id) }
    }
}

fun diaperUIToDiaperRecord(diaperUIState: DiaperUIState): DiaperRecord {
    val createdAtEpoch = LocalDateTime
        .of(diaperUIState.selectedDate, diaperUIState.selectedTime)
        .toEpochSecond(ZoneOffset.UTC)
    return DiaperRecord(
        babyId = diaperUIState.babyId!!,
        diaperType = diaperUIState.diaperType!!,
        attachmentURL = diaperUIState.attachmentURL,
        notes = diaperUIState.notes,
        createdAtEpoch = createdAtEpoch,
        soilState = diaperUIState.diaperSoilState.toList()
    )

}