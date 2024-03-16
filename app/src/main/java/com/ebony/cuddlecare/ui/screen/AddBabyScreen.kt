package com.ebony.cuddlecare.ui.screen

import android.os.Build
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.DropDownField
import com.ebony.cuddlecare.ui.components.SaveButton
import com.ebony.cuddlecare.ui.components.SwitchWithIcon
import com.ebony.cuddlecare.ui.components.ToggableButton
import com.ebony.cuddlecare.ui.viewmodel.AddBabyViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBaby(
    navController: NavController,
    addBabyViewModel: AddBabyViewModel = viewModel()
            ){
                val addBabyUIState by addBabyViewModel.addBabyUIState.collectAsState()
                val scope = rememberCoroutineScope()
                val bottomSheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                )
                var openBottomSheet by remember{ mutableStateOf(false) }
                MainContent(onClick = {openBottomSheet = true})
                if (openBottomSheet){
                    ModalBottomSheet(
                        sheetState = bottomSheetState,
                        onDismissRequest = { openBottomSheet = false},
                        dragHandle = {
                            Column(modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                BottomSheetDefaults.DragHandle()
                                Text(text = "Enter Baby Details", style = MaterialTheme.typography.titleLarge)
                                Spacer(modifier = Modifier.height(10.dp))
                                Divider()
                            }
                        }
                    ) {
                        SheetContent(
                            onNavigateToMainActivity = {
                                navController.navigate(Screen.HomeScreen.name)
                            }

                           )
                    }
                }


            }


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun SheetContent(
        addBabyViewModel: AddBabyViewModel = viewModel(),
        onNavigateToMainActivity: () -> Unit)
    {
        val addBabyUIState by addBabyViewModel.addBabyUIState.collectAsState()
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ){
            item { GenderSelector() }
            item{Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                var text by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = text,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { text = it },
                    shape = CircleShape,
                    label = { Text("Baby Name") })
            }}
            item{Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Birth Date: ")
                DateInput1(toggleDatePicker = {
                    addBabyViewModel.toggableDatePicker() },
                    isDateExpanded = addBabyUIState.isDateExpanded,
                    selectedDate = addBabyUIState.selectedDate,
                    setSelectedDate = addBabyViewModel::setSelectedDate)

            }}
            item{ Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Premature?")
                SwitchWithIcon()
            }}
            item{SaveButton(onClick={
                onNavigateToMainActivity()
            })}
        }


    }

    @Composable
    fun MainContent(onClick: ()  -> Unit){
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(

                Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                ) {
                Image(
                    painter = painterResource(id = R.drawable.addbaby_icon),
                    contentDescription = null,
                    Modifier
                        .size(350.dp)
                        .padding(bottom = 32.dp, top = 32.dp),
                    contentScale = ContentScale.Crop
                )

                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .height(70.dp)
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Text("Add Baby", fontSize = 30.sp)

                }
            }
        }}

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DateInput1(
        modifier: Modifier = Modifier,
        toggleDatePicker: () -> Unit,
        isDateExpanded: Boolean = false,
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
            label = "dd-MMM-yyyy",
            value = selectedDate.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")),
            modifier = modifier.padding(horizontal = 4.dp)
        ) {
            toggleDatePicker()
        }

        if (isDateExpanded) {
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





    @Composable
@Preview(showBackground = true)
fun GenderSelector() {
    Row(
        modifier = Modifier
            .background(Color.LightGray, shape = RoundedCornerShape(50))
            .height(50.dp)
            .fillMaxWidth()
    ) {
        var isEnabled by remember { mutableStateOf(true) }


        ToggableButton(
            activated = isEnabled,
            modifier = Modifier
                .height(50.dp)
                .weight(1f),
            onClick = { isEnabled = true }) {
            Text(text = "Boy",fontSize = 18.sp)
        }
        ToggableButton(
            activated = !isEnabled,
            modifier = Modifier
                .height(50.dp)
                .weight(1f),
            onClick = { isEnabled =false }) {
            Text(text = "Girl",fontSize = 18.sp)
        }

    }
}





