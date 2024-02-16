package drawable


import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.PunchClock
import androidx.compose.material.icons.filled.Timer
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
import com.ebony.cuddlecare.ui.components.mTopBar
import com.ebony.cuddlecare.ui.screen.AttachmentRow
import com.ebony.cuddlecare.ui.screen.BreastFeedingControlBtn
import com.ebony.cuddlecare.ui.screen.DateInput
import com.ebony.cuddlecare.ui.screen.LastUpdated
import com.ebony.cuddlecare.ui.screen.LeadingDetailsIcon
import com.ebony.cuddlecare.ui.screen.SaveButton
import com.ebony.cuddlecare.ui.screen.ScreenMainIcon
import com.ebony.cuddlecare.ui.screen.TimeInput
import com.ebony.cuddlecare.ui.viewmodel.DiaperViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun MedicineScreen(diaperViewModel: DiaperViewModel = viewModel()) {
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

            ScreenMainIcon(R.drawable.medicine_logo)
            LastUpdated("Medication")
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
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "0 oz ")
                    }
                }
            }



                AttachmentRow()
                SaveButton()
                //TODO disable save button if nothing is entered

            }
        }
    }








