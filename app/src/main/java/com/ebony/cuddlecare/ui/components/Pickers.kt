package com.ebony.cuddlecare.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DateInput(
    modifier: Modifier = Modifier,
    toggleDatePicker: () -> Unit,
    isTimeExpanded: Boolean = false,
    selectedDate: LocalDate,
    setSelectedDate: (Long) -> Unit = {}
) {

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli(),
        selectableDates = PastOrPresentSelectableDates
    )
    DropDownField(
        label = "",
        value = selectedDate.format(DateTimeFormatter.ofPattern("d MMM")),
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        toggleDatePicker()
    }

    if (isTimeExpanded) {
        DatePickerDialog(
            onDismissRequest = { toggleDatePicker() },
            confirmButton = {
                TextButton(onClick = { toggleDatePicker() }) {
                    setSelectedDate(datePickerState.selectedDateMillis ?: 0L)
                    Text("OK")
                }
            },

            ) {
            DatePicker(state = datePickerState, showModeToggle = true, title = {})
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
private object PastOrPresentSelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis <= System.currentTimeMillis()
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year <= LocalDate.now().year
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeInput(
    modifier: Modifier = Modifier,
    label: String,
    showTimeDialog: Boolean = false,
    setTimePicker: (Boolean) -> Unit,
    onValueChange: (LocalTime) -> Unit = {},
    selectedTime: LocalTime = LocalTime.now(),
) {

    val timeState = rememberTimePickerState(
        initialHour = selectedTime.hour,
        initialMinute = selectedTime.minute
    )

    if (showTimeDialog) {
        BasicAlertDialog(
            onDismissRequest = { setTimePicker(false) },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .background(color = Color.LightGray.copy(alpha = .3f))
                    .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(state = timeState)
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { setTimePicker(false) },
                        content = { Text(text = "Dismiss") })
                    TextButton(
                        onClick = {
                            setTimePicker(false)
                            onValueChange(LocalTime.of(timeState.hour, timeState.minute))
                        },
                        content = { Text(text = "Confirm") })
                }
            }
        }
    }

    DropDownField(
        modifier = modifier,
        label = label,
        value = selectedTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
        onClick = { setTimePicker(true) })

}