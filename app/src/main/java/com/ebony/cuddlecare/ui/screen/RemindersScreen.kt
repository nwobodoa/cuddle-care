package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.MTopBar
import com.ebony.cuddlecare.ui.components.ToggableButton
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
            Column (modifier=Modifier.padding(it)){

                NavHost(navController = navController, startDestination = "setting") {
                    composable("setting") {
                        ReminderSetting()
                    }
                    composable("form") {
                        ReminderForm(reminderUIState.selectedReminderType)
                    }
                }

                if (isOpen) {
                    SetReminderModal(
                        sheetState, onClose = { isOpen = false },
                        setReminderCategory = {
                            reminderViewModel.setSelectedReminderType(it)
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

val reminderCategories = listOf(
    ReminderCategory(R.drawable.bf, "Breastfeeding"),
    ReminderCategory(R.drawable.bottle, "Bottle"),
    ReminderCategory(R.drawable.diaper, "Diaper"),
    ReminderCategory(R.drawable.crib, "Sleeping"),
    ReminderCategory(R.drawable.medicine, "Medication"),
    ReminderCategory(R.drawable.vaccine, "Vaccination"),
)

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

    ModalBottomSheet(sheetState = sheetState,
        onDismissRequest = {
            onClose()
        }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(text = "Reminder")
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.Center
            ) {
                items(reminderCategories.size) { idx ->
                    ReminderGridItem(category = reminderCategories[idx]) {
                        setReminderCategory(it)
                    }
                }
            }

            Button(onClick = {
                onClose()
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Cancel", fontSize = 20.sp)
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
        Text(text = "Reminders")

        Row {
            Text(text = "Allow notifications")
        }

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(text = "Show notification for baby 'David'")
            MySwitch()

        }

    }
}

@Composable
fun ReminderForm(selectedReminderType: String?) {
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
            Text(text = "Reminder: $selectedReminderType")

            var isEnabled by remember { mutableStateOf(true) }
            Row(modifier = Modifier.fillMaxWidth()) {
                ToggableButton(
                    activated = isEnabled,
                    onClick = { isEnabled = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Basic")
                }
                ToggableButton(
                    activated = !isEnabled,
                    onClick = { isEnabled = false },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Advanced")
                }
            }
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            )
            {
                if (isEnabled) {
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
                        MySwitch()
                    }
                } else {
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
                        MySwitch()

                    }
                }

                SaveButton()
            }
        }
    }
}


@Composable
fun MySwitch() {
    var checked by remember { mutableStateOf(false) }
    Switch(checked = checked, onCheckedChange = {
        checked = it
    }, thumbContent = if (checked) {
        {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        null

    })
}






