package com.ebony.cuddlecare.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.DropDownField
import com.ebony.cuddlecare.ui.components.MTopBar
import com.ebony.cuddlecare.ui.components.ToggableButton
import com.ebony.cuddlecare.ui.viewmodel.DiaperViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun RecordDiaperStateScreen(diaperViewModel: DiaperViewModel = viewModel()) {
    val diaperUIState by diaperViewModel.diaperUIState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.orange))
    ) {
        MTopBar()
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(color = colorResource(id = R.color.backcolor))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {

            ScreenMainIcon(R.drawable.diaper_logo)
            LastUpdated("Diaper")
            TimeTypeSegment()
            DiaperCount(diaperUIState.diaperCount)
            AttachmentRow()
            SaveButton()
            //TODO disable save button if nothing is entered

        }
    }
}

@Composable
fun ScreenMainIcon(drawableId: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = null,
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun LastUpdated(title: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = title, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(text = "Last: 20mins ago")
    }
}

@Composable
private fun DiaperCount(count: Int) {

    Row {
        Text(
            text = "Diaper Count : $count", fontSize = 14.sp, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(6.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun TimeTypeSegment(diaperViewModel: DiaperViewModel = viewModel()) {
    val diaperUIState by diaperViewModel.diaperUIState.collectAsState()

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
                modifier = Modifier.weight(1f),
                title = "Time",
                imageVector = Icons.Default.AccessTime,
                contentDescription = "Time"
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.weight(1f)
            ) {
                DateInput(
                    toggleDatePicker = { diaperViewModel.toggleDatePicker() },
                    isTimeExpanded = diaperUIState.isTimeExpanded,
                    selectedDate = diaperUIState.selectedDate,
                    setSelectedDate = diaperViewModel::setSelectedDate
                )
                Spacer(modifier = Modifier.size(8.dp))
                TimeInput(
                    setTimePicker = diaperViewModel::setShowTimePicker,
                    label = "",
                    showTimeDialog = diaperUIState.showTimePicker,
                    selectedTime = diaperUIState.selectedTime,
                    onValueChange = diaperViewModel::setSelectedTime,
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            LeadingDetailsIcon(
                title = "Type",
                imageVector = Icons.Outlined.Folder,
                contentDescription = "Folder icon"
            )
            Row {
                Text(text = "Select Type")
                Icon(Icons.Outlined.ArrowDropDown, contentDescription = "Arrow down icon")
            }
        }

        Row(
            horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()


        ) {

            ToggableButton(
                modifier = Modifier.clip(shape = RoundedCornerShape(30.dp)),
                onClick = { diaperViewModel.toggleWetDiaper() },
                activated = diaperUIState.isWetDiaper
            ) {
                Icon(
                    imageVector = Icons.Default.WaterDrop, contentDescription = "water drop"
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Wet")

            }

            ToggableButton(
                onClick = {
                    diaperViewModel.toggleDirtyDiaper()
                },
                activated = diaperUIState.isDirtyDiaper,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clip(shape = RoundedCornerShape(30.dp))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.poop),
                    contentDescription = "Poop",

                    )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Dirty")
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
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
            .toEpochMilli()
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
                    setSelectedDate(datePickerState.selectedDateMillis?:0L)
                    Text("OK")
                }
            },

            ) {
            DatePicker(state = datePickerState, showModeToggle = true, title = {})
        }
    }

}


@Composable
fun AttachmentRow() {
    val maxLength = 3000
    var inputValue by remember { mutableStateOf("") }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = inputValue,
        onValueChange = {
            if (it.length <= maxLength) inputValue = it
        },

        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = colorResource(id = R.color.backcolor),
            unfocusedIndicatorColor = Color.Gray,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        keyboardOptions = KeyboardOptions(autoCorrect = true),
        label = { Text(text = "Notes") },

        )
    Text(
        text = "${inputValue.length}/$maxLength",
        textAlign = TextAlign.End,
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
    )

    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Attachments", modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Gallery icon",
            modifier = Modifier.align(Alignment.CenterVertically)

        )
        Icon(
            imageVector = Icons.Default.CameraAlt,
            contentDescription = "Camera icon",
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}


@Composable
fun LeadingDetailsIcon(
    modifier: Modifier = Modifier,
    title: String,
    imageVector: ImageVector,
    contentDescription: String
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(imageVector = imageVector, contentDescription = contentDescription)
        Text(title)
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun ExposedSelectionMenu(
    title: String, options: List<String>, onSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }
    TextField(
        readOnly = true,
        modifier = Modifier.clickable(onClick = { expanded = !expanded }),
        value = selectedOptionText,
        onValueChange = { },
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
        trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(
                expanded = expanded
            )
        },
        placeholder = { Text(text = title) },
        colors = ExposedDropdownMenuDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Gray,
            unfocusedIndicatorColor = Color.Gray,
            disabledIndicatorColor = Color.Transparent,
        )

    )
    DropdownMenu(expanded = expanded, onDismissRequest = {
        expanded = false

    }) {
        options.forEachIndexed { index: Int, selectionOption: String ->
            DropdownMenuItem(onClick = {
                selectedOptionText = selectionOption
                expanded = false
                onSelected(index)
            }) {
                Text(text = selectionOption)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
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
        AlertDialog(
            onDismissRequest = { setTimePicker(false) },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.background)
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



