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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.mTopBar
import com.ebony.cuddlecare.ui.viewmodel.DiaperViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun BottleFeeding(diaperViewModel: DiaperViewModel = viewModel()) {
    val diaperUIState by diaperViewModel.diaperUIState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.orange))
    )
    {
        mTopBar()
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(color = colorResource(id = R.color.backcolor))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ScreenMainIcon(R.drawable.bottlescreen_logo)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    LastUpdated("Bottle")
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
                Row(modifier = Modifier.fillMaxWidth()){

                    Image(painter = painterResource(id = R.drawable.minusbtn), contentDescription = "minus button")
                    Text(text="0")
                    Text(text="ml")
                    Image(painter = painterResource(id = R.drawable.plusbtn), contentDescription = "plus button")


                }
            }
            AttachmentRow()
            SaveButton()
        }

    }
}