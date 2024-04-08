package com.ebony.cuddlecare.ui.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.documents.Document
import com.ebony.cuddlecare.ui.documents.SortableActivity
import com.ebony.cuddlecare.ui.documents.activeBabyCollection
import com.ebony.cuddlecare.ui.screen.TimerState
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


data class SleepingUIState(
    val durationSecs: Long = 0,
    val pauseSecs: Long = 0,
    val startTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime? = null,
    val timerState: TimerState = TimerState.STOPPED,
    val notes: String = "",
    val savedSuccess: Boolean = false,
    val loading: Boolean = false,
    val sleepRecords: List<SleepRecord> = emptyList()
)

data class SleepRecord(
    val id: String,
    val durationSecs: Long,
    val pauseSecs: Long,
    val startTimeEpochSecs: Long,
    val notes: String,
    val endTimeEpochSecs: Long
) : SortableActivity {
    constructor() : this("", 0, 0, 0, "", 0)

    override fun rank(): Long {
        return endTimeEpochSecs
    }
}

class SleepingViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val sleepingCollection = db.collection(Document.Sleeping.name)
    private val _sleepingUIState = MutableStateFlow(SleepingUIState())
    val sleepingUIState = _sleepingUIState.asStateFlow()

    fun reset() {
        _sleepingUIState.update { SleepingUIState() }
    }

    fun toggleTimerState() {
        _sleepingUIState.update {
            val updatedTimerState = when (it.timerState) {
                TimerState.STARTED -> TimerState.PAUSED
                TimerState.STOPPED, TimerState.PAUSED -> TimerState.STARTED
            }
            it.copy(timerState = updatedTimerState)
        }
    }

    fun incrementTimer() {
        _sleepingUIState.update {
            it.copy(durationSecs = it.durationSecs + 1)
        }
    }

    fun incrementPauseTimer() {
        _sleepingUIState.update {
            it.copy(pauseSecs = it.pauseSecs + 1)
        }

    }

    private fun setSavedSuccess(state: Boolean) {
        _sleepingUIState.update { it.copy(savedSuccess = state) }
    }

    private fun sleepUIStateToSleepingRecord(): SleepRecord {
        val sleepingUIState = _sleepingUIState.value

        return SleepRecord(
            id = "",
            durationSecs = sleepingUIState.durationSecs,
            pauseSecs = sleepingUIState.pauseSecs,
            startTimeEpochSecs = localDateTimeToEpoch(sleepingUIState.startTime)!!,
            endTimeEpochSecs = localDateTimeToEpoch(
                sleepingUIState.endTime ?: LocalDateTime.now()
            )!!,
            notes = sleepingUIState.notes
        )
    }

    fun save(activeBaby: Baby?) {
        val sleepingUIState = _sleepingUIState.value
        if (activeBaby == null || sleepingUIState.durationSecs == 0L) {
            TODO("Handle empty record")
            return
        }
        setSavedSuccess(false)
        setLoading(true)

        val ref = activeBabyCollection(sleepingCollection, activeBaby).document()
        val record = sleepUIStateToSleepingRecord().copy(id = ref.id)

        ref.set(record)
            .addOnSuccessListener {
                setSavedSuccess(true)
            }.addOnFailureListener {
                setSavedSuccess(false)
            }.addOnCompleteListener {
                setLoading(false)
            }
    }

    private fun setLoading(state: Boolean) {
        _sleepingUIState.update { it.copy(loading = state) }
    }

    fun setNotes(notes: String) {
        _sleepingUIState.update { it.copy(notes = notes) }
    }

    fun fetchRecord(activeBaby: Baby, day: LocalDate) {
        val startOfDayEpoch = day.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
        val endOfDayEpoch = day.atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC)

        setLoading(true)
        activeBabyCollection(sleepingCollection, activeBaby)
            .whereLessThanOrEqualTo("endTimeEpochSecs", endOfDayEpoch)
            .whereGreaterThanOrEqualTo("endTimeEpochSecs", startOfDayEpoch)
            .addSnapshotListener { snap, ex ->
                setLoading(false)
                if (ex != null) {
                    Log.e(TAG, "fetchRecord: ", ex)
                    return@addSnapshotListener
                }

                val sleepRecords =
                    snap?.documents?.mapNotNull { it.toObject(SleepRecord::class.java) }
                        ?: emptyList()
                _sleepingUIState.update { it.copy(sleepRecords = sleepRecords) }
            }
    }

}