package com.ebony.cuddlecare.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Folder
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.LeadingDetailsIcon
import com.ebony.cuddlecare.ui.components.MTopBar
import com.ebony.cuddlecare.ui.viewmodel.VaccinationViewModel
import com.ebony.cuddlecare.ui.viewmodel.VaccineUIState


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun VaccinationScreen(
    vaccinationViewModel: VaccinationViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val vaccineUIState by vaccinationViewModel.vaccineUIState.collectAsState()
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
            TimeTypeSegment(vaccinationViewModel, vaccineUIState)
            AttachmentRow()
            SaveButton()
            //TODO disable save button if nothing is entered
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TimeTypeSegment(
    vaccinationViewModel: VaccinationViewModel,
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
                    toggleDatePicker = { vaccinationViewModel.toggleDatePicker() },
                    isTimeExpanded = vaccineUIState.isTimeExpanded,
                    selectedDate = vaccineUIState.selectedDate,
                    setSelectedDate = vaccinationViewModel::setSelectedDate
                )
                Spacer(modifier = Modifier.size(8.dp))
                TimeInput(
                    setTimePicker = vaccinationViewModel::setShowTimePicker,
                    label = "",
                    showTimeDialog = vaccineUIState.showTimePicker,
                    selectedTime = vaccineUIState.selectedTime,
                    onValueChange = vaccinationViewModel::setSelectedTime,
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
            Row {
                Text(text = "Select Type")
                Icon(Icons.Outlined.ArrowDropDown, contentDescription = "Arrow down icon")
            }
        }
    }
}
@Composable
fun VaccineType() {
    val vaccineType = listOf(
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
}

