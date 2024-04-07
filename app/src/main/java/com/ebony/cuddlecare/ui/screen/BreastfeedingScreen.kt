package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.AttachmentRow
import com.ebony.cuddlecare.ui.components.LeadingDetailsIcon
import com.ebony.cuddlecare.ui.components.MTopBar
import com.ebony.cuddlecare.ui.components.SaveButton
import com.ebony.cuddlecare.ui.components.ToggableButton
import com.ebony.cuddlecare.ui.viewmodel.BreastSideState
import com.ebony.cuddlecare.ui.viewmodel.BreastfeedingUIState
import com.ebony.cuddlecare.util.localDateTimeToDate
import com.ebony.cuddlecare.util.localDateTimeToTime
import com.ebony.cuddlecare.util.secondsToFormattedString
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


fun buttonImageVector(timerState: TimerState): ImageVector {
    return when (timerState) {
        TimerState.STARTED -> Icons.Default.Pause
        TimerState.STOPPED -> Icons.Default.PlayArrow
        TimerState.PAUSED -> Icons.Default.PlayArrow
    }
}

fun buttonText(breastSideState: BreastSideState): String {
    val stateText = when (breastSideState.timerState) {
        TimerState.STARTED -> "Stop"
        TimerState.STOPPED -> "Start"
        TimerState.PAUSED -> "Resume"
    }
    return "(${breastSideState.side})$stateText"
}

//TODO Add attachment
@Composable
fun BreastfeedingScreen(
    breastfeedingUIState: BreastfeedingUIState,
    rightBreastUIState: BreastSideState,
    leftBreastUIState: BreastSideState,
    onNavigateBack: () -> Unit = {},
    toggleRightButton: () -> Unit,
    toggleLeftButton: () -> Unit,
    increaseRightTimer: () -> Unit,
    increaseLeftTimer: () -> Unit,
    incrementPauseTimer: () -> Unit,
    onNotesValueChange: (String) -> Unit,
    saveBreastFeeding: () -> Unit,
    resetSaved: () -> Unit
) {
    LaunchedEffect(key1 = rightBreastUIState.timerState, key2 = leftBreastUIState.timerState) {
        while (rightBreastUIState.timerState == TimerState.PAUSED && leftBreastUIState.timerState == TimerState.PAUSED) {
            delay(1.seconds)
            incrementPauseTimer()
        }
    }

    LaunchedEffect(breastfeedingUIState.saved) {
        if (breastfeedingUIState.saved) {
            onNavigateBack()
            resetSaved()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.orange))
    )
    {
        MTopBar(onNavigateBack = onNavigateBack)
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(color = colorResource(id = R.color.backcolor))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ScreenMainIcon(R.drawable.breastfeeding_logo)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    LastUpdated("Breastfeeding", "Last: Never")
                }
                Row {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add icon")
                    Icon(imageVector = Icons.Default.Delete, contentDescription = " Delete icon")
                }

            }

            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = Color.White)
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            )

            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BreastFeedingControlBtn(
                        modifier = Modifier.weight(1f),
                        breastSideState = leftBreastUIState,
                        onClick = toggleLeftButton,
                        increaseTimer = increaseLeftTimer,
                    )
                    BreastFeedingControlBtn(
                        modifier = Modifier.weight(1f),
                        breastSideState = rightBreastUIState,
                        onClick = toggleRightButton,
                        increaseTimer = increaseRightTimer,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = Color.White)
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            )
            {


                Row {
                    LeadingDetailsIcon(
                        title = "Duration", imageVector = Icons.Default.Timelapse,
                        contentDescription = "time elapsed icon"
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = secondsToFormattedString(
                                leftBreastUIState.activeSeconds
                                        + rightBreastUIState.activeSeconds
                            )
                        )
                    }
                }

                Row {
                    LeadingDetailsIcon(
                        title = "Pause", imageVector = Icons.Default.Pause,
                        contentDescription = "pause icon"
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = secondsToFormattedString(
                                breastfeedingUIState.pauseSeconds
                            )
                        )
                    }
                }
                Row {
                    LeadingDetailsIcon(
                        title = "Started", imageVector = Icons.Default.Timer,
                        contentDescription = "timer icon"
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = localDateTimeToDate(breastfeedingUIState.startTime)
                        )
                        Text(
                            text = " ${localDateTimeToTime(breastfeedingUIState.startTime)}"
                        )
                    }
                }
                Row {
                    LeadingDetailsIcon(
                        title = "Ended", imageVector = Icons.Default.Flag,
                        contentDescription = "timer icon"
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "${localDateTimeToDate(breastfeedingUIState.endTime)} ")
                        Text(text = " ${localDateTimeToTime(breastfeedingUIState.endTime)}")
                    }
                }
            }
            AttachmentRow(value = breastfeedingUIState.notes, onValueChange = onNotesValueChange)
            SaveButton(onClick = saveBreastFeeding)
        }
    }
}

enum class TimerState {
    STARTED, STOPPED, PAUSED
}


@Composable
fun BreastFeedingControlBtn(
    modifier: Modifier = Modifier,
    breastSideState: BreastSideState,
    increaseTimer: () -> Unit,
    onClick: () -> Unit
) {
    val activated = breastSideState.timerState == TimerState.STARTED

    LaunchedEffect(key1 = breastSideState.timerState) {
        while (activated) {
            delay(1.seconds)
            increaseTimer()
        }
    }


    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        ToggableButton(
            activated = activated,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = buttonImageVector(breastSideState.timerState),
                contentDescription = "play icon"
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = buttonText(breastSideState))
        }
        Text(text = secondsToFormattedString(breastSideState.activeSeconds))
    }
}