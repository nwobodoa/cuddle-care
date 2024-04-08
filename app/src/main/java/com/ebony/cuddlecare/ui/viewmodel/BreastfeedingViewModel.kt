package com.ebony.cuddlecare.ui.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.documents.Document
import com.ebony.cuddlecare.ui.documents.SortableActivity
import com.ebony.cuddlecare.ui.documents.activeBabyCollection
import com.ebony.cuddlecare.ui.screen.TimerState
import com.ebony.cuddlecare.util.epochSecondsToLocalDateTime
import com.ebony.cuddlecare.util.localDateTimeToEpoch
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

data class BreastSideState(
    val activeSeconds: Long = 0,
    val timerState: TimerState = TimerState.STOPPED,
    val btnActivate: Boolean = false,
    val side: String = "L"
)

data class BreastSideRecord(
    val activeSeconds: Long = 0L,
    val side: String = ""
)

data class BreastFeedingRecord(
    val id: String = "",
    val startTime: Long = 0,
    val endTime: Long? = null,
    val notes: String = "",
    val attachmentLink: String = "",
    val pauseSeconds: Long = 0,
    val leftBreast: BreastSideRecord? = null,
    val rightBreast: BreastSideRecord? = null
) : SortableActivity {
    constructor() : this("", 0, null, "", "", 0, null, null)

    override fun rank(): Long {
        return endTime ?: 0
    }

}

fun breastFeedingUIStateToRecord(uiState: BreastfeedingUIState): BreastFeedingRecord {
    return BreastFeedingRecord(
        startTime = localDateTimeToEpoch(uiState.startTime)!!,
        endTime = localDateTimeToEpoch(uiState.endTime ?: LocalDateTime.now()),
        notes = uiState.notes,
        attachmentLink = uiState.attachmentLink,
        pauseSeconds = uiState.pauseSeconds,
        leftBreast = uiState.leftBreast?.let {
            BreastSideRecord(
                activeSeconds = uiState.leftBreast.activeSeconds,
                side = uiState.leftBreast.side
            )
        },
        rightBreast = uiState.rightBreast?.let {
            BreastSideRecord(
                activeSeconds = uiState.rightBreast.activeSeconds,
                side = uiState.rightBreast.side
            )
        }
    )

}


data class BreastfeedingUIState(
    val startTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime? = null,
    val notes: String = "",
    val attachmentLink: String = "",
    val pauseSeconds: Long = 0,
    val leftBreast: BreastSideState? = null,
    val rightBreast: BreastSideState? = null,
    val saved: Boolean = false,
    val loading: Boolean = false,
    val breastfeedingRecords: List<BreastFeedingRecord> = emptyList()
)


class BreastfeedingViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val breastfeedingCollection = db.collection(Document.BreastFeeding.name)
    private val _breastfeedingState: MutableStateFlow<BreastfeedingUIState> = MutableStateFlow(
        BreastfeedingUIState()
    )
    private val _leftBreastState: MutableStateFlow<BreastSideState> =
        MutableStateFlow(BreastSideState())
    private val _rightBreastState: MutableStateFlow<BreastSideState> = MutableStateFlow(
        BreastSideState(side = "R")
    )
    val breastfeedingUIState: StateFlow<BreastfeedingUIState> = _breastfeedingState.asStateFlow()
    val leftBreastUIState = _leftBreastState.asStateFlow()
    val rightBreastUIState = _rightBreastState.asStateFlow()


    fun fetchRecords(activeBaby: Baby, day: LocalDate) {
        val startOfDayEpoch = day.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
        val endOfDayEpoch = day.atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC)

        setLoading(true)
        activeBabyCollection(breastfeedingCollection, activeBaby)
            .whereLessThanOrEqualTo("endTime", endOfDayEpoch)
            .whereGreaterThanOrEqualTo("endTime", startOfDayEpoch)
            .addSnapshotListener { snap, ex ->
                setLoading(false)
                if (ex != null) {
                    Log.e(TAG, "fetchRecords: ", ex)
                    return@addSnapshotListener
                }
                snap?.documents
                    ?.mapNotNull { it.toObject(BreastFeedingRecord::class.java) }
                    ?.let { records ->
                        _breastfeedingState.update {
                            it.copy(
                                breastfeedingRecords = records
                            )
                        }
                    }
            }
    }

    private fun setLoading(b: Boolean) {
        _breastfeedingState.update { it.copy(loading = b) }
    }


    fun reset() {
        _breastfeedingState.update { BreastfeedingUIState() }
        _leftBreastState.update { BreastSideState(side = "L") }
        _rightBreastState.update { BreastSideState(side = "R") }
    }


    fun incrementLeftTimer() {
        incrementTimer(_leftBreastState)
    }

    fun incrementRightTimer() {
        incrementTimer(_rightBreastState)
    }

    private fun incrementTimer(breastSide: MutableStateFlow<BreastSideState>) {
        breastSide.update { it.copy(activeSeconds = it.activeSeconds + 1) }
    }


    fun onNotesValueChange(value: String) {
        _breastfeedingState.update { it.copy(notes = value) }
    }

    fun incrementPauseTimer() {
        _breastfeedingState.update { it.copy(pauseSeconds = it.pauseSeconds + 1) }
    }


    fun toggleRightBtnActiveState() {
        _rightBreastState.update { r ->
            var rightState = r.timerState
            _leftBreastState.update { l ->
                var leftState = l.timerState
                if (rightState == TimerState.PAUSED || rightState == TimerState.STOPPED) {
                    rightState = TimerState.STARTED
                    if (leftState == TimerState.STARTED) {
                        leftState = TimerState.PAUSED
                    }
                } else {
                    rightState = TimerState.PAUSED
                }
                l.copy(timerState = leftState)
            }
            r.copy(timerState = rightState)
        }
    }

    fun toggleLeftBtnActiveState() {
        _rightBreastState.update { r ->
            var rightState = r.timerState
            _leftBreastState.update { l ->
                var leftState = l.timerState
                if (leftState == TimerState.PAUSED || leftState == TimerState.STOPPED) {
                    leftState = TimerState.STARTED
                    if (rightState == TimerState.STARTED) {
                        rightState = TimerState.PAUSED
                    }
                } else {
                    leftState = TimerState.PAUSED
                }
                l.copy(timerState = leftState)
            }
            r.copy(timerState = rightState)
        }
    }

    fun setSaved(saved: Boolean) {
        _breastfeedingState.update { it.copy(saved = saved) }
    }

    fun save(activeBaby: Baby?) {
        Log.i(TAG, "save: $activeBaby")
        if (activeBaby == null) return

        Log.i(TAG, "save after null: $activeBaby")

        _breastfeedingState.update {
            it.copy(
                rightBreast = _rightBreastState.value,
                leftBreast = _leftBreastState.value,
                saved = true,
                endTime = LocalDateTime.now(),
                loading = true
            )
        }

        val ref = activeBabyCollection(breastfeedingCollection, activeBaby).document()
        val record = breastFeedingUIStateToRecord(_breastfeedingState.value)

        ref.set(
            record.copy(id = ref.id)
        ).addOnSuccessListener {
            reset()
            _breastfeedingState.update { it.copy(saved = true) }
        }.addOnFailureListener {
            Log.e(TAG, "breastfeeding save: ", it)
        }.addOnCompleteListener {
            Log.e(TAG, "breastfeeding save complete: ")
        }
    }


}