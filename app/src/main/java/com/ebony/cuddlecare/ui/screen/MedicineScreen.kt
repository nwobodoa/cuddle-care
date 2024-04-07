package com.ebony.cuddlecare.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.AttachmentRow
import com.ebony.cuddlecare.ui.components.DateInput
import com.ebony.cuddlecare.ui.components.DropDownField
import com.ebony.cuddlecare.ui.components.LeadingDetailsIcon
import com.ebony.cuddlecare.ui.components.MTopBar
import com.ebony.cuddlecare.ui.components.SaveButton
import com.ebony.cuddlecare.ui.components.TimeInput
import com.ebony.cuddlecare.ui.viewmodel.MedicineViewModel


@Composable
@Preview(showBackground = true)
fun MedicineScreen(
    medicineViewModel: MedicineViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val medicineUIState by medicineViewModel.medicineUIState.collectAsState()
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
        ) {

            ScreenMainIcon(R.drawable.medicine_logo)
            LastUpdated("Medication", "Last: Never")
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
                        horizontalArrangement = Arrangement.End, modifier = Modifier.weight(1f)
                    ) {
                        DateInput(
                            toggleDatePicker = { medicineViewModel.toggleDatePicker() },
                            isTimeExpanded = medicineUIState.isTimeExpanded,
                            selectedDate = medicineUIState.selectedDate,
                            setSelectedDate = medicineViewModel::setSelectedDate
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        TimeInput(
                            setTimePicker = medicineViewModel::setShowTimePicker,
                            label = "",
                            showTimeDialog = medicineUIState.showTimePicker,
                            selectedTime = medicineUIState.selectedTime,
                            onValueChange = medicineViewModel::setSelectedTime,
                        )

                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LeadingDetailsIcon(
                        modifier = Modifier.weight(1f),
                        title = "Dosage",
                        imageVector = Icons.Default.Medication,
                        contentDescription = "Time"
                    )
                    Box {

                        Row(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = medicineUIState.qty.toString())
                            Spacer(modifier = Modifier.size(8.dp))
                            DropDownField(
                                value = medicineUIState.selectedUnit,
                                onClick = { medicineViewModel.setIsUnitDropdownExpanded(true) })
                            DropdownMenu(
                                expanded = medicineUIState.isUnitDropdownExpanded,
                                onDismissRequest = {
                                    medicineViewModel.setIsUnitDropdownExpanded(false)
                                }) {
                                medicineUIState.units.forEachIndexed { idx, unit ->
                                    DropdownMenuItem(
                                        text = { Text(text = unit, color = Color.Black) },
                                        onClick = {
                                            medicineViewModel.setSelectedUnit(unit)
                                            medicineViewModel.setIsUnitDropdownExpanded(false)
                                        })
                                    if (idx != medicineUIState.units.lastIndex) {
                                        HorizontalDivider()
                                    }
                                }
                            }

                        }
                    }

                }

            }



            AttachmentRow()
            SaveButton(onClick = {/*TODO*/ })
            //TODO disable save button if nothing is entered

        }
    }
}









