package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.AttachmentRow
import com.ebony.cuddlecare.ui.components.DateInput
import com.ebony.cuddlecare.ui.components.LeadingDetailsIcon
import com.ebony.cuddlecare.ui.components.MTopBar
import com.ebony.cuddlecare.ui.components.SaveButton
import com.ebony.cuddlecare.ui.components.TimeInput
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.viewmodel.VaccinationViewModel
import com.ebony.cuddlecare.ui.viewmodel.VaccineUIState
import java.time.LocalTime


@Composable
fun VaccinationScreen(
    vaccinationViewModel: VaccinationViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    activeBaby: Baby?
) {
    val vaccineUIState by vaccinationViewModel.vaccineUIState.collectAsState()

    if (vaccineUIState.showVaccineList) {
        VaccineList(
            setSelectedVaccine = vaccinationViewModel::setSelectVaccine,
            closeList = { vaccinationViewModel.setShowVaccineList(false) }
        )
        return
    }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.orange))
    ) {
        MTopBar(onNavigateBack = onNavigateBack)
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(color = colorResource(id = R.color.backcolor))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            ScreenMainIcon(drawableId = R.drawable.vac_logo)
            LastUpdated(title = "Vaccination", "Last: Never")
            TimeTypeSegment(
                setSelectDate = vaccinationViewModel::setSelectedDate,
                toggleDatePicker = vaccinationViewModel::toggleDatePicker,
                setShowTimePicker = vaccinationViewModel::setShowTimePicker,
                setSelectedTime = vaccinationViewModel::setSelectedTime,
                setShowVaccineList = vaccinationViewModel::setShowVaccineList,
                vaccineUIState = vaccineUIState
            )
            AttachmentRow(value = vaccineUIState.notes, onValueChange = vaccinationViewModel::setNotes)
            SaveButton(onClick = { vaccinationViewModel.save(activeBaby) })
            //TODO disable save button if nothing is entered
        }
    }
}

@Composable
private fun TimeTypeSegment(
    setSelectDate: (Long) -> Unit,
    toggleDatePicker: () -> Unit,
    setShowTimePicker: (Boolean) -> Unit,
    setSelectedTime: (LocalTime) -> Unit,
    setShowVaccineList: (Boolean) -> Unit,
    vaccineUIState: VaccineUIState
) {
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
                    toggleDatePicker = toggleDatePicker,
                    isTimeExpanded = vaccineUIState.isTimeExpanded,
                    selectedDate = vaccineUIState.selectedDate,
                    setSelectedDate = setSelectDate
                )
                Spacer(modifier = Modifier.size(8.dp))
                TimeInput(
                    setTimePicker = setShowTimePicker,
                    label = "",
                    showTimeDialog = vaccineUIState.showTimePicker,
                    selectedTime = vaccineUIState.selectedTime,
                    onValueChange = setSelectedTime,
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
                contentDescription = "Time"
            )
            Row(modifier = Modifier.clickable {
                setShowVaccineList(true)
            }) {
                Text(text = "Select Type")
                Icon(Icons.Outlined.ArrowDropDown, contentDescription = "Arrow down icon")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VaccineList(
    setSelectedVaccine: (String) -> Unit = {},
    closeList: () -> Unit
) {
    val vaccineTypes =
        listOf(
            "Chickenpox (Var)",
            "Diphtheria, tetanus, & whooping cough (DTap)",
            "Haemophilus Influenzae type b(Hib)",
            "Hepatitis A (Hep A)",
            "Hepatitis B (Hep B)",
            "Influenza (Flu)",
            "Measles, mumps, rubella (MMR)",
            "Meningococcal (MenB)",
            "Pneumococcal (PCV)",
            "Polio (IPV)",
            "Rotavirus (RV)",
            "Tuberculosis (BCG)"

        )

    val filteredList = remember { mutableStateOf(vaccineTypes) }
    val searchTerm = remember { mutableStateOf("") }

    LaunchedEffect(key1 = searchTerm.value) {
        if (searchTerm.value.isEmpty()) {
            filteredList.value = vaccineTypes
            return@LaunchedEffect
        }
        filteredList.value =
            vaccineTypes.filter { it.contains(searchTerm.value, ignoreCase = true) }

    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Scaffold(
            topBar = { MTopBar(onNavigateBack = {}) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {},
                    containerColor = colorResource(id = R.color.myRed),
                    contentColor = Color.White, shape = CircleShape
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            }) {
            LazyColumn(
                modifier = Modifier
                    .background(Color.White)
                    .padding(it)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            )
            {
                stickyHeader {
                    Text(
                        text = "Select Vaccine", fontWeight = FontWeight.Bold, fontSize = 18.sp
                    )
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Vaccination")
                        IconButton(onClick = { /*TODO*/ }) {
                            Icons.Filled.AddCircle
                        }
                    }
                }
                item {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = "Search") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = null
                            )
                        },
                        value = searchTerm.value,
                        onValueChange = { c -> searchTerm.value = c })
                }

                items(filteredList.value) { vaccine ->
                    Row(modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp)
                        .clickable {
                            setSelectedVaccine(vaccine)
                            closeList()
                        }

                    ) {
                        Text(text = vaccine)
                    }
                    HorizontalDivider()
                }

            }

        }
    }
}



