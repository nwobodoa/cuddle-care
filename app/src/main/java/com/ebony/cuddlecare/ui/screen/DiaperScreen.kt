package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.AttachmentRow
import com.ebony.cuddlecare.ui.components.DateInput
import com.ebony.cuddlecare.ui.components.ErrorText
import com.ebony.cuddlecare.ui.components.LeadingDetailsIcon
import com.ebony.cuddlecare.ui.components.Loading
import com.ebony.cuddlecare.ui.components.MTopBar
import com.ebony.cuddlecare.ui.components.SaveButton
import com.ebony.cuddlecare.ui.components.TimeInput
import com.ebony.cuddlecare.ui.components.ToggableButton
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.documents.DiaperSoilType
import com.ebony.cuddlecare.ui.documents.DiaperType
import com.ebony.cuddlecare.ui.viewmodel.DiaperUIState
import com.ebony.cuddlecare.ui.viewmodel.DiaperViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
@Preview(showBackground = true)
fun DiaperScreen(
    diaperViewModel: DiaperViewModel = viewModel(),
    activeBaby: Baby? = null,
    onNavigateBack: () -> Unit = {}
) {
    LaunchedEffect(key1 = activeBaby) {
        diaperViewModel.setActiveBaby(activeBaby)
        diaperViewModel.fetchDiaperCount(activeBaby)
    }

    val diaperUIState by diaperViewModel.diaperUIState.collectAsState()

    LaunchedEffect(key1 = diaperUIState.savedSuccessfully) {
        if (diaperUIState.savedSuccessfully) {
            onNavigateBack()
        }
    }



    Scaffold(topBar = { MTopBar(onNavigateBack = onNavigateBack) })
    {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxHeight()
                .background(color = colorResource(id = R.color.orange))
        ) {

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
                LastUpdated("Diaper", "Last: 20 mins ago")

                Column(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(color = Color.White)
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                )

                {
                    TimeControlRow(
                        diaperUIState = diaperUIState,
                        toggleDatePicker = diaperViewModel::toggleDatePicker,
                        setSelectedDate = diaperViewModel::setSelectedDate,
                        setShowTimePicker = diaperViewModel::setShowTimePicker,
                        setSelectedTime = diaperViewModel::setSelectedTime,
                    )
                    DiaperTypeSelectRow(
                        diaperUIState = diaperUIState,
                        setSelectedDiaperType = { type -> diaperViewModel.setSelectedDiaperType(type) },
                        setShowDiaperTypeDropDown = diaperViewModel::setShowDiaperTypeDropdown,
                    )
                    DiaperSoilTypeBtnRow(
                        diaperUIState = diaperUIState,
                        toggleDirtyDiaper = diaperViewModel::toggleDirtyDiaper,
                        toggleWetDiaper = diaperViewModel::toggleWetDiaper
                    )
                }

                if (diaperUIState.diaperType == DiaperType.DISPOSABLE) {
                    DiaperCount(
                        count = diaperUIState.diaperCount?.count ?: "0",
                        onClick = { diaperViewModel.setShowDiaperRefill(true) },
                    )
                }
                AttachmentRow(
                    value = diaperUIState.notes,
                    onValueChange = diaperViewModel::setNotes
                )

                Column {
                    diaperUIState.errors.forEach { e -> ErrorText(e) }
                }

                SaveButton(onClick = diaperViewModel::save)

                RefillAlertDialog(
                    onDismissRequest = { diaperViewModel.setDiaperCountWarning(false) },
                    onConfirmation = {
                        diaperViewModel.setDiaperCountWarning(false)
                        diaperViewModel.setShowDiaperRefill(true)
                    },
                    showDialog = diaperUIState.showDiaperWarning && diaperUIState.diaperType == DiaperType.DISPOSABLE && diaperUIState.diaperCount?.let {
                        (it.count.toIntOrNull() ?: 0) <= 20
                    } == true,
                    dialogTitle = "Diapers Running low!",
                    dialogText = "Diaper count is less than 20. The total number of diapers left is ${diaperUIState.diaperCount?.count ?: 0}",
                    icon = Icons.Default.WarningAmber
                )

                DiaperCountInput(
                    isOpen = diaperUIState.showDiaperRefill,
                    onClose = { diaperViewModel.setShowDiaperRefill(false) },
                    onConfirm = diaperViewModel::saveDiaperCount,
                    diaperCount = diaperUIState.diaperCount?.count ?: "0",
                    setDiaperCount = { count ->
                        diaperViewModel.setDiaperCount(
                            activeBaby,
                            count
                        )
                    },
                    loading = diaperUIState.loading
                )
            }
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
fun LastUpdated(title: String, text: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp))
    {
        Text(text = title, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(text = text)
    }
}

@Composable
private fun DiaperCount(
    count: String,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        Row {
            Text(
                text = "Diaper Count : $count",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(6.dp))
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Refill Diaper", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onClick) {
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
            }
        }
    }
}

@Composable
private fun DiaperTypeSelectRow(
    diaperUIState: DiaperUIState,
    setSelectedDiaperType: (DiaperType) -> Unit,
    setShowDiaperTypeDropDown: (Boolean) -> Unit,
) {
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

        Column {
            Row(modifier = Modifier.clickable { setShowDiaperTypeDropDown(true) }) {
                val displayType =
                    if (diaperUIState.diaperType == DiaperType.NONE) "Select Type" else diaperUIState.diaperType?.name?.lowercase()
                        ?.replaceFirstChar { it.uppercase() }
                Text(text = displayType ?: "Select Type")
                Icon(Icons.Outlined.ArrowDropDown, contentDescription = "Arrow down icon")
            }
            DropdownMenu(
                expanded = diaperUIState.showDiaperTypeDropdown,
                onDismissRequest = { setShowDiaperTypeDropDown(false) }
            ) {
                val diaperTypeEntries = DiaperType.entries.filter { it != DiaperType.NONE }
                diaperTypeEntries.forEachIndexed { idx, type ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                                color = Color.Black
                            )
                        },
                        onClick = {
                            setSelectedDiaperType(type)
                            setShowDiaperTypeDropDown(false)
                        })
                    if (idx != diaperTypeEntries.lastIndex) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }

}

@Composable
private fun TimeControlRow(
    diaperUIState: DiaperUIState,
    toggleDatePicker: () -> Unit,
    setSelectedDate: (Long) -> Unit,
    setShowTimePicker: (Boolean) -> Unit,
    setSelectedTime: (LocalTime) -> Unit,
) {

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
                toggleDatePicker = toggleDatePicker,
                isTimeExpanded = diaperUIState.isTimeExpanded,
                selectedDate = diaperUIState.selectedDate,
                setSelectedDate = setSelectedDate
            )
            Spacer(modifier = Modifier.size(8.dp))
            TimeInput(
                setTimePicker = setShowTimePicker,
                label = "",
                showTimeDialog = diaperUIState.showTimePicker,
                selectedTime = diaperUIState.selectedTime,
                onValueChange = setSelectedTime,
            )
        }
    }
}


@Composable
fun DiaperSoilTypeBtnRow(
    diaperUIState: DiaperUIState,
    toggleDirtyDiaper: () -> Unit,
    toggleWetDiaper: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()

    ) {
        ToggableButton(
            modifier = Modifier.clip(shape = RoundedCornerShape(30.dp)),
            onClick = toggleWetDiaper,
            activated = diaperUIState.diaperSoilState.contains(DiaperSoilType.WET)
        ) {
            Icon(
                imageVector = Icons.Default.WaterDrop, contentDescription = "water drop"
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = "Wet")

        }

        ToggableButton(
            onClick = toggleDirtyDiaper,
            activated = diaperUIState.diaperSoilState.contains(DiaperSoilType.DIRTY),
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

@Composable
fun RefillAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    showDialog: Boolean
) {

    if (showDialog) {
        AlertDialog(
            icon = {
                Icon(icon, contentDescription = null)
            },
            title = {
                Text(text = dialogTitle)
            },
            text = {
                Text(text = dialogText)
            },
            onDismissRequest = onDismissRequest,
            confirmButton = {
                Button(
                    onClick = onConfirmation
                ) {
                    Text("Refill Diaper")
                }
            },
            dismissButton = {
                OutlinedButton(
                    border = BorderStroke(1.dp, colorResource(id = R.color.orange)),
                    onClick = onDismissRequest
                ) {
                    Text("Dismiss", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaperRefillSheet(
    bottomSheetState: SheetState,
    onClose: () -> Unit,
    onConfirm: () -> Unit,
    diaperCount: String,
    setDiaperCount: (String) -> Unit,
    loading: Boolean
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .background(Color.White)
            .wrapContentSize()
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        val onBottomSheetClose = {
            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                if (!bottomSheetState.isVisible) {
                    onClose()
                }
            }
        }
        if (loading) {
            Loading()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {


            Text(text = "Refill Diaper", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        TextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = colorResource(id = R.color.backcolor),
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            prefix = { Text(text = "Enter new Diaper Count: ") },
            modifier = Modifier.fillMaxWidth(),
            value = diaperCount,
            onValueChange = { setDiaperCount(it) },
            label = { Text(text = "") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        HorizontalDivider()
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                border = BorderStroke(1.dp, colorResource(id = R.color.orange)),
                modifier = Modifier.padding(end = 16.dp),
                onClick = { onBottomSheetClose() }
            ) {
                Text(text = "Cancel", fontWeight = FontWeight.Bold)
            }
            Button(onClick = {
                onConfirm()
                onBottomSheetClose()
            }) {
                Text(text = "Ok")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaperCountInput(
    isOpen: Boolean,
    onClose: () -> Unit,
    onConfirm: () -> Unit,
    setDiaperCount: (String) -> Unit,
    diaperCount: String,
    loading: Boolean
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (isOpen) {
        ModalBottomSheet(
            onDismissRequest = onClose,
            sheetState = bottomSheetState,
            dragHandle = null
        )
        {
            DiaperRefillSheet(
                bottomSheetState,
                onClose,
                onConfirm,
                diaperCount = diaperCount,
                setDiaperCount = setDiaperCount,
                loading = loading
            )
        }
    }
}
