package com.ebony.cuddlecare.ui.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.documents.BottleFeedingRecord
import com.ebony.cuddlecare.ui.documents.Document
import com.ebony.cuddlecare.ui.documents.activeBabyCollection
import com.ebony.cuddlecare.util.epochMillisToDate
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import kotlin.math.max

data class BottleFeedingUIState(
    val quantityMl: Long = 0,
    val notes: String = "",
    val attachmentURL: String = "",
    val isTimeExpanded: Boolean = false,
    val showTimePicker: Boolean = false,
    val showDatePicker: Boolean = false,
    val selectedTime: LocalTime = LocalTime.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val loading: Boolean = false,
    val canNavigateBack: Boolean = false,
    val bottleFeedingRecords: List<BottleFeedingRecord> = emptyList(),
    val breastFeedingRecords: List<BreastFeedingRecord> = emptyList()
)

class BottleFeedViewModel : ViewModel() {
    private val _bottleFeedingUIState: MutableStateFlow<BottleFeedingUIState> =
        MutableStateFlow(BottleFeedingUIState())
    private val quantitySteps: Int = 5
    val bottleFeedingUIState = _bottleFeedingUIState.asStateFlow()

    private val db = Firebase.firestore
    private val bottleFeedCollection = db.collection(Document.BottleFeeding.name)

    fun incrementQty() {
        _bottleFeedingUIState.update { it.copy(quantityMl = it.quantityMl + quantitySteps) }
    }

    fun decrementQty() {
        _bottleFeedingUIState.update { it.copy(quantityMl = max(0, it.quantityMl - quantitySteps)) }
    }

    fun toggleDatePicker() {
        _bottleFeedingUIState.update { it.copy(isTimeExpanded = !it.isTimeExpanded) }
    }

    private fun uIStateToRecord(): BottleFeedingRecord {
        val state = _bottleFeedingUIState.value
        return BottleFeedingRecord(
            id = "",
            babyId = "",
            quantityMl = state.quantityMl,
            notes = state.notes,
            attachmentURL = state.attachmentURL,
            timestamp = LocalDateTime.of(state.selectedDate, state.selectedTime)
                .toEpochSecond(
                    ZoneOffset.UTC
                )
        )
    }

    fun fetchRecords(activeBaby: Baby?, day: LocalDate) {
        if (activeBaby == null) return
        val startOfDayEpoch = day.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
        val endOfDayEpoch = day.atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC)

        activeBabyCollection(bottleFeedCollection,activeBaby)
            .whereLessThanOrEqualTo("timestamp", endOfDayEpoch)
            .whereGreaterThanOrEqualTo("timestamp", startOfDayEpoch)
            .addSnapshotListener { doc, ex ->
                if (ex != null) {
                    Log.e(TAG, "fetchBottleFeed: ", ex)
                    return@addSnapshotListener
                }
                val bottleFeeds: List<BottleFeedingRecord>? = doc?.documents?.mapNotNull {
                    it.toObject(
                        BottleFeedingRecord::class.java
                    )
                }
                _bottleFeedingUIState.update {
                    it.copy(bottleFeedingRecords = bottleFeeds ?: emptyList())
                }
            }
    }


    fun setSelectedDate(dateEpochMilli: Long) {
        _bottleFeedingUIState.update { it.copy(selectedDate = epochMillisToDate(dateEpochMilli)) }

    }

    fun setShowTimePicker(isVisible: Boolean) {
        _bottleFeedingUIState.update { it.copy(showTimePicker = isVisible) }
    }

    fun setSelectedTime(localTime: LocalTime) {
        _bottleFeedingUIState.update { it.copy(selectedTime = localTime) }
    }

    fun setNotes(notes: String) {
        _bottleFeedingUIState.update { it.copy(notes = notes) }
    }

    fun setLoading(loading: Boolean) {
        _bottleFeedingUIState.update { it.copy(loading = loading) }
    }

    fun setCanNavigateBack(canNavigateBack: Boolean) {
        _bottleFeedingUIState.update { it.copy(canNavigateBack = canNavigateBack) }
    }


    fun save(activeBaby: Baby?) {
        val recordToSave = uIStateToRecord()
        if (recordToSave == BottleFeedingRecord() || activeBaby == null) {
            TODO("Handle empty record")
            return
        }

        setLoading(true)
        val ref = activeBabyCollection(bottleFeedCollection,activeBaby).document()

        ref.set(recordToSave.copy(id = ref.id))
            .addOnSuccessListener {
                setCanNavigateBack(true)
                // TODO Handle success
            }
            .addOnFailureListener {
                // TODO Handle failure
            }
            .addOnCompleteListener {
                setLoading(true)
            }

    }

}