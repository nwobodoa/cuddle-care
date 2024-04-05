package com.ebony.cuddlecare.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.ebony.cuddlecare.ui.screen.TimerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime

data class BreastSideState(
    val activeSeconds: Long = 0,
    val timerState: TimerState = TimerState.STOPPED,
    val btnActivate: Boolean = false,
    val side: String = "L"
)

data class BreastfeedingUIState(
    val startTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime? = null,
    val notes: String = "",
    val attachmentLink: String = "",
    val pauseSeconds: Long = 0,
)


class BreastfeedingViewModel : ViewModel() {
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


}