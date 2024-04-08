package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.AttachmentRow
import com.ebony.cuddlecare.ui.components.DateInput
import com.ebony.cuddlecare.ui.components.LeadingDetailsIcon
import com.ebony.cuddlecare.ui.components.MTopBar
import com.ebony.cuddlecare.ui.components.SaveButton
import com.ebony.cuddlecare.ui.components.TimeInput
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.viewmodel.BottleFeedViewModel

@Preview
@Composable
fun DoctorVisitScreen(
    bottleFeedingViewModel: BottleFeedViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    activeBaby: Baby? = null
) {


    val bottleFeedingUIState by bottleFeedingViewModel.bottleFeedingUIState.collectAsState()

    LaunchedEffect(key1 = bottleFeedingUIState.canNavigateBack) {
        if (bottleFeedingUIState.canNavigateBack) {
            onNavigateBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.orange))
    )
    {
        MTopBar(onNavigateBack = onNavigateBack, activeBaby = activeBaby)
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(color = colorResource(id = R.color.backcolor))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ScreenMainIcon(R.drawable.doc_logo)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    LastUpdated("Doctor Visit", "Last: Never")
                }
            }
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
                            toggleDatePicker = bottleFeedingViewModel::toggleDatePicker,
                            isTimeExpanded = bottleFeedingUIState.isTimeExpanded,
                            selectedDate = bottleFeedingUIState.selectedDate,
                            setSelectedDate = bottleFeedingViewModel::setSelectedDate
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        TimeInput(
                            setTimePicker = bottleFeedingViewModel::setShowTimePicker,
                            label = "",
                            showTimeDialog = bottleFeedingUIState.showTimePicker,
                            selectedTime = bottleFeedingUIState.selectedTime,
                            onValueChange = bottleFeedingViewModel::setSelectedTime,
                        )

                    }
                }
            }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(text = "Reaction")
                    }
                    IconButton(onClick = bottleFeedingViewModel::incrementQty) {
//                        Image(
//                            painter = painterResource(id = R.drawable.happyface1),
//                            contentDescription = "plus button"
//                        )
                    }

//
//                    Image(
//
//                        painter = painterResource(id = R.drawable.straightface1),
//                        contentDescription = "plus button"
//                    )
//
//                    IconButton(onClick = bottleFeedingViewModel::incrementQty) {
//                        Image(
//                            painter = painterResource(id = R.drawable.sad1),
//                            contentDescription = "plus button"
//                        )
//                    }
                }

                AttachmentRow(
                    value = bottleFeedingUIState.notes,
                    onValueChange = bottleFeedingViewModel::setNotes
                )
                SaveButton(onClick = { bottleFeedingViewModel.save(activeBaby) })
            }
        }
    }



