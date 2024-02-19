package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.outlined.AlarmOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.LeadingDetailsIcon
import com.ebony.cuddlecare.ui.components.MTopBar
import com.ebony.cuddlecare.ui.components.SwitchWithIcon
import com.ebony.cuddlecare.ui.components.ToggableButton
import com.ebony.cuddlecare.ui.viewmodel.ReminderType
import com.ebony.cuddlecare.ui.viewmodel.ReminderUIState
import com.ebony.cuddlecare.ui.viewmodel.ReminderViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ReminderScreen(reminderViewModel: ReminderViewModel = viewModel()) {
    val reminderUIState by reminderViewModel.reminderUIState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val navController: NavHostController = rememberNavController()

    var isOpen by rememberSaveable {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.orange))
    )
    {
        Scaffold(
            topBar = { MTopBar(navController) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        isOpen = true
                    }, containerColor = colorResource(id = R.color.myRed),
                    contentColor = Color.White, shape = CircleShape
                ) {
                    Icon(Icons.Filled.Add, "Floating action button.")
                }
            }
        ) {
            Column(modifier = Modifier.padding(it)) {
                NavHost(navController = navController, startDestination = "form") {
                    composable("setting") {
                        ReminderSetting()
                    }
                    composable("form") {
                        ReminderForm(reminderUIState.reminderSubject, reminderViewModel)
                    }
                }

                if (isOpen) {
                    SetReminderModal(
                        sheetState, onClose = { isOpen = false },
                        setReminderCategory = {
                            reminderViewModel.setSelectedReminderSubject(it)
                            navController.navigate("form")
                            isOpen = false
                        }
                    )
                }
            }
        }

    }
}


data class ReminderCategory(val iconId: Int, val name: String)


@Composable
fun ReminderGridItem(category: ReminderCategory, setCategory: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(modifier = Modifier.size(75.dp),
            onClick = { setCategory(category.name) }) {
            Image(
                modifier = Modifier.size(75.dp),
                painter = painterResource(id = category.iconId),
                contentDescription = "${category.name} icon"
            )
        }
        Text(category.name)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SetReminderModal(
    sheetState: SheetState,
    onClose: () -> Unit,
    setReminderCategory: (String) -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onClose
    )
    {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            Text(text = "Reminder", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            ReminderCategoryGrid { setReminderCategory(it) }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                onClick = onClose
            ) {
                Text(text = "Cancel", fontSize = 24.sp)
            }
        }
    }
}

@Composable
private fun ReminderCategoryGrid(setReminderCategory: (String) -> Unit) {
    val reminderCategories = listOf(
        ReminderCategory(R.drawable.bf, "Breastfeeding"),
        ReminderCategory(R.drawable.bottle, "Bottle"),
        ReminderCategory(R.drawable.diaper, "Diaper"),
        ReminderCategory(R.drawable.crib, "Sleeping"),
        ReminderCategory(R.drawable.medicine, "Medication"),
        ReminderCategory(R.drawable.vaccine, "Vaccination"),
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        items(reminderCategories.size) { idx ->
            ReminderGridItem(category = reminderCategories[idx]) {
                setReminderCategory(it)
            }
        }
    }
}

@Composable
fun ReminderSetting(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(color = colorResource(id = R.color.backcolor))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Reminders", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = Color.White)
        ) {

            Row(modifier = Modifier.padding(top = 16.dp, start = 8.dp)) {
                Text(text = "Allow notifications", fontWeight = FontWeight.Bold)
            }

            Row(
                Modifier
                    .padding(start = 8.dp, end = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(text = "Show notification for baby 'David'")
                SwitchWithIcon()

            }
        }

    }
}

@Composable
fun ReminderForm(selectedReminderType: String? = null, reminderViewModel: ReminderViewModel) {
    val reminderUIState by reminderViewModel.reminderUIState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.orange))
    )
    {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(color = colorResource(id = R.color.backcolor))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Reminder: $selectedReminderType",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            FormControlButtonGroup(reminderViewModel)

            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            )
            {
                if (reminderUIState.reminderType == ReminderType.BASIC) {
                    BasicReminderForm(reminderViewModel = reminderViewModel)
                } else {
                    AdvancedReminderForm(reminderViewModel = reminderViewModel)
                }
            }
            SaveButton()
        }
    }
}

@Composable
private fun FormControlButtonGroup(
    reminderViewModel: ReminderViewModel
) {
    val reminderUIState: ReminderUIState by reminderViewModel.reminderUIState.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(50))
    ) {

        ToggableButton(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            activated = reminderUIState.reminderType == ReminderType.BASIC,
            onClick = { reminderViewModel.setSelectedReminderType(ReminderType.BASIC) },
        ) {
            Text(text = "Basic")
        }

        ToggableButton(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            activated = reminderUIState.reminderType == ReminderType.ADVANCED,
            onClick = { reminderViewModel.setSelectedReminderType(ReminderType.ADVANCED) },

            ) {
            Text(text = "Advanced")
        }
    }
}


@Composable
fun BasicReminderForm(reminderViewModel: ReminderViewModel) {
    val reminderUIState by reminderViewModel.reminderUIState.collectAsState()
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        LeadingDetailsIcon(
            title = "Every",
            imageVector = Icons.Default.Alarm,
            contentDescription = "Alarm clock icon"
        )
        Text(text = "3h")
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        LeadingDetailsIcon(
            title = "Do not disturb",
            imageVector = Icons.Outlined.AlarmOff,
            contentDescription = "Alarm off icon"
        )
        SwitchWithIcon(
            isChecked = reminderUIState.basicReminder.isDoNotDisturb,
            onCheck = reminderViewModel::setDoNotDisturb
        )


    }
    if (reminderUIState.basicReminder.isDoNotDisturb) {
        Row {
            Text(text = "Do not disturb me")
        }
    }
}

@Composable
fun AdvancedReminderForm(reminderViewModel: ReminderViewModel) {
    val reminderUIState by reminderViewModel.reminderUIState.collectAsState()
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        LeadingDetailsIcon(
            title = "Date",
            imageVector = Icons.Default.CalendarToday,
            contentDescription = "calendar icon"
        )
        Text(text = "Today, 24 Nov")
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        LeadingDetailsIcon(
            title = "Time",
            imageVector = Icons.Default.Alarm,
            contentDescription = "Alarm icon"
        )
        Text(text = "3:00 PM")
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        LeadingDetailsIcon(
            title = "",
            imageVector = Icons.Default.Repeat,
            contentDescription = "repeat icon"
        )
        SwitchWithIcon(
            isChecked = reminderUIState.advancedReminder.isRepeated,
            onCheck = reminderViewModel::setReminderRepeated
        )

    }

    if (reminderUIState.advancedReminder.isRepeated) {
        Text(text = "Lets repeat")
    }
}