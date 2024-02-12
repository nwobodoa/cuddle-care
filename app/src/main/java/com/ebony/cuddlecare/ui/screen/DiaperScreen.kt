package com.ebony.cuddlecare.ui.screen

import android.app.TimePickerDialog
import android.os.Build
import android.widget.TimePicker
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
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
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.ebony.cuddlecare.ui.components.ToggableButton
import com.ebony.cuddlecare.ui.components.mTopBar
import com.ebony.cuddlecare.ui.viewmodel.DiaperViewModel
import java.time.LocalTime
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
        mTopBar()
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
            LastUpdated()
            extracted()
            DiaperCount(diaperUIState.diaperCount)
            AttachmentRow()
            SaveButton()
            //TODO disable save button if nothing is entered

        }
    }
}

@Composable
private fun ScreenMainIcon(drawableId: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = null,
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun LastUpdated() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        Text(text = "Diaper", fontSize = 28.sp, fontWeight = FontWeight.Bold)
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun extracted(diaperViewModel: DiaperViewModel = viewModel()) {
    val diaperUIState by diaperViewModel.diaperUIState.collectAsState()

    Column(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
            .padding(8.dp)
            .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)
    )

    {
        Row(
//            modifier = Modifier.fillMaxWidth(),
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
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                DateInput(
                    modifier = Modifier.weight(1f),
                    toggleDatePicker = { diaperViewModel.toggleDatePicker() },
                    isTimeExpanded = diaperUIState.isTimeExpanded
                )
                TimePicker(modifier = Modifier.weight(1f), label = "")
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            val groups = listOf("Disposable", "Cloth")
            var selectedIndex by remember { mutableStateOf(0) }

            LeadingDetailsIcon(
                title = "Type",
                imageVector = Icons.Outlined.Folder,
                contentDescription = "Folder icon"
            )

//            ExposedSelectionMenu(title = "Select group", options = groups, onSelected = { index ->
//                selectedIndex = index
//            })
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
                enabled = diaperUIState.isWetDiaper
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
                enabled = diaperUIState.isDirtyDiaper,
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DateInput(
    modifier: Modifier = Modifier, toggleDatePicker: () -> Unit, isTimeExpanded: Boolean = false
) {
    val datePickerState = rememberDatePickerState()
    DropDownField(
        label = "", value = "9 Nov",
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        toggleDatePicker()
    }

    if (isTimeExpanded) {
        DatePickerDialog(
            onDismissRequest = { toggleDatePicker() },
            confirmButton = {
                TextButton(onClick = { toggleDatePicker() }) {
                    Text("OK")
                }
            },

        ) {
            DatePicker(state = datePickerState, showModeToggle = true, title = {})
        }
    }

}
//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TimePickerWithDialog() {
//    var selectedHour by remember { mutableIntStateOf(0) }
//    var selectedMinute by remember { mutableIntStateOf(0) }
//    var showDialog by remember { mutableStateOf(false) }
//    val timeState = rememberTimePickerState(
//        initialHour = selectedHour,
//        initialMinute = selectedMinute
//    )
//
//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = { showDialog = false },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Column(
//                modifier = Modifier
//                    .background(color = Color.LightGray.copy(alpha = .3f))
//                    .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                TimePicker(state = timeState)
//                Row(
//                    modifier = Modifier
//                        .padding(top = 12.dp)
//                        .fillMaxWidth(), horizontalArrangement = Arrangement.End
//                ) {
//                    TextButton(onClick = { showDialog = false }) {
//                        Text(text = "Dismiss")
//                    }
//                    TextButton(onClick = {
//                        showDialog = false
//                        selectedHour = timeState.hour
//                        selectedMinute = timeState.minute
//                    }) {
//                        Text(text = "Confirm")
//                    }
//                }
//            }
//        }
//    }
//
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Button(onClick = { showDialog = true }) {
//            Text(text = "Show Time Picker")
//        }
//        Text(text = "Time is ${timeState.hour} : ${timeState.minute}")
//    }
//}



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


@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    label: String,
    value: String = "23:10",
    onValueChange: (LocalTime) -> Unit = {},
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    pattern: String = "HH:mm",
    is24HourView: Boolean = false,
    initialTime: LocalTime = LocalTime.now(),
    readOnly: Boolean = false
) {

    var timeString by remember {
        mutableStateOf(
            initialTime.format(
                DateTimeFormatter.ofPattern(
                    pattern
                )
            )
        )
    }
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val time = if (value.isNotBlank()) LocalTime.parse(value, formatter) else LocalTime.now()
    val dialog = TimePickerDialog(
        LocalContext.current,
        { _, hour, minute ->
            timeString = LocalTime.of(hour, minute).toString()
            onValueChange(LocalTime.of(hour, minute))
        },
        time.hour,
        time.minute,
        is24HourView,
    )

    TextField(
        label = { Text(label) },
        value = timeString,
        onValueChange = {},
        enabled = readOnly,
        readOnly = readOnly,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Gray,
            unfocusedIndicatorColor = Color.Gray,
            disabledIndicatorColor = Color.Transparent,
        ),
        modifier = modifier
            .clickable(onClick = {
                if (!readOnly) {
                    dialog.show()
                }
            })
    )


}



