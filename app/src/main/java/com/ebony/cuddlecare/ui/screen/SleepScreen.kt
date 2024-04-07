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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.LeadingDetailsIcon
import com.ebony.cuddlecare.ui.components.MTopBar
import com.ebony.cuddlecare.ui.components.SaveButton
import com.ebony.cuddlecare.ui.components.ToggableButton
import com.ebony.cuddlecare.ui.viewmodel.SleepingUIState
import com.ebony.cuddlecare.util.localDateTimeToDate
import com.ebony.cuddlecare.util.localDateTimeToEpoch
import com.ebony.cuddlecare.util.localDateTimeToTime
import com.ebony.cuddlecare.util.secondsToFormattedString
import kotlinx.coroutines.delay
import kotlin.reflect.KFunction0
import kotlin.time.Duration.Companion.seconds


@Composable
fun SleepingScreen(
    onNavigateBack: () -> Unit = {},
    sleepingUIState: SleepingUIState,
    setNotes: (String) -> Unit,
    incrementPauseTimer: KFunction0<Unit>,
    incrementTimer: KFunction0<Unit>,
    saveSleep: () -> Unit,
    toggleTimerState: () -> Unit,
    reset: () -> Unit
) {
    @Composable
    fun ButtonIcon() {
        when (sleepingUIState.timerState) {
            TimerState.STARTED ->
                Icon(
                    imageVector = Icons.Default.Pause,
                    contentDescription = "play icon"
                )

            TimerState.STOPPED, TimerState.PAUSED ->
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "play icon"
                )
        }
    }

    val buttonTxt = when (sleepingUIState.timerState) {
        TimerState.STARTED -> "Pause"
        TimerState.STOPPED -> "Start"
        TimerState.PAUSED -> "Resume"
    }

    LaunchedEffect(key1 = sleepingUIState.savedSuccess) {
        if (sleepingUIState.savedSuccess) {
            reset()
            onNavigateBack()
        }
    }

    LaunchedEffect(key1 = sleepingUIState.timerState) {
        while (sleepingUIState.timerState == TimerState.PAUSED) {
            delay(1.seconds)
            incrementPauseTimer()
        }

        while (sleepingUIState.timerState == TimerState.STARTED) {
            delay(1.seconds)
            incrementTimer()
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
            ScreenMainIcon(R.drawable.sleep_logo)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    LastUpdated("Sleeping", "Last: Never")
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
                        .fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    ToggableButton(
                        activated = sleepingUIState.timerState == TimerState.STARTED,
                        onClick = toggleTimerState
                    ) {
                        ButtonIcon()
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = buttonTxt)
                    }
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LeadingDetailsIcon(
                        title = "Duration", imageVector = Icons.Default.Timelapse,
                        contentDescription = "timeelapsed icon"
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = secondsToFormattedString(sleepingUIState.durationSecs))
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
                        Text(text = secondsToFormattedString(sleepingUIState.pauseSecs))
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
                        Text(text = localDateTimeToDate(sleepingUIState.startTime))
                        Spacer(modifier = Modifier.size(5.dp))
                        Text(text = localDateTimeToTime(sleepingUIState.startTime))
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
                        Text(text = localDateTimeToDate(sleepingUIState.endTime))
                        Text(text = localDateTimeToTime(sleepingUIState.endTime))
                    }
                }
            }
            AttachmentRow(value = sleepingUIState.notes, onValueChange = setNotes)
            SaveButton(onClick = saveSleep)
        }
    }
}

