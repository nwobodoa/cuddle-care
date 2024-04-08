package com.ebony.cuddlecare.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.material.icons.outlined.Vaccines
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.documents.DiaperRecord
import com.ebony.cuddlecare.ui.documents.DiaperSoilType
import com.ebony.cuddlecare.ui.documents.SortableActivity
import com.ebony.cuddlecare.ui.viewmodel.BreastFeedingRecord
import com.ebony.cuddlecare.ui.viewmodel.SleepRecord
import com.ebony.cuddlecare.ui.viewmodel.VaccinationRecord
import com.ebony.cuddlecare.util.epochSecondsToLocalDateTime
import com.ebony.cuddlecare.util.secondsToFormattedTime
import com.ebony.cuddlecare.util.timestampToString
import java.time.format.DateTimeFormatter

@Composable
fun DetailedActivityList(sortableActivities: List<SortableActivity>) {
    val sortedActivities = sortableActivities.sortedBy { -it.rank() }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
        verticalArrangement = Arrangement.Center
    )
    {
        sortedActivities.mapIndexed { idx, record ->
            when (record) {
                is DiaperRecord -> DiaperDetailRow(diaperRecord = record)
                is BreastFeedingRecord -> BreastfeedingDetailRow(record = record)
                is SleepRecord -> SleepingDetailRow(record = record)
                is VaccinationRecord -> VaccinationDetailRow(record = record)
                else -> Text(text = "Unknown")
            }

            if (idx != sortableActivities.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun DiaperDetailRow(diaperRecord: DiaperRecord) {
    Row(
        modifier = Modifier.padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(35.dp),
            painter = painterResource(id = R.drawable.diaper), contentDescription = null
        )
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(text = diaperRecord.timestampToString())

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (diaperRecord.soilState.contains(DiaperSoilType.DIRTY)) {
                    Image(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 8.dp),
                        painter = painterResource(id = R.drawable.pee),
                        contentDescription = null
                    )
                    Text(text = "Pee")
                }
                if (diaperRecord.soilState.contains(DiaperSoilType.WET)) {
                    Image(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        painter = painterResource(id = R.drawable.poop),
                        contentDescription = null
                    )
                    Text(text = "Poo")
                }
            }

        }
    }
}


@Composable
private fun BreastfeedingDetailRow(record: BreastFeedingRecord) {
    val startTime = timestampToString(record.startTime)
    val endTime = timestampToString(record.endTime)

    val rightActiveSeconds = record.rightBreast?.activeSeconds ?: 0
    val leftActiveSeconds = record.leftBreast?.activeSeconds ?: 0

    val totalTime = rightActiveSeconds + leftActiveSeconds

    Row(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(35.dp),
            painter = painterResource(id = R.drawable.bf), contentDescription = null
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
        ) {
            Text(text = "$startTime - $endTime")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.padding(end = 8.dp),
                    imageVector = Icons.Outlined.Timelapse,
                    contentDescription = null
                )
                Text(
                    text = "${secondsToFormattedTime(totalTime)} Left: ${
                        secondsToFormattedTime(
                            leftActiveSeconds
                        )
                    } Right: ${secondsToFormattedTime(rightActiveSeconds)}"
                )

            }
        }
    }
}

@Composable
private fun SleepingDetailRow(record: SleepRecord) {
    val startTime = timestampToString(record.startTimeEpochSecs)
    val endTime = timestampToString(record.endTimeEpochSecs)


    val totalTime = record.endTimeEpochSecs - record.startTimeEpochSecs

    Row(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(35.dp),
            painter = painterResource(id = R.drawable.crib), contentDescription = null
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
        ) {
            Text(text = "$startTime - $endTime")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.padding(end = 8.dp),
                    imageVector = Icons.Outlined.Timelapse,
                    contentDescription = null
                )
                Text(
                    text = secondsToFormattedTime(totalTime)
                )

            }
        }
    }
}


@Composable
private fun VaccinationDetailRow(record: VaccinationRecord) {
    val endTime =
        epochSecondsToLocalDateTime(record.endTimeEpochSecs)?.format(DateTimeFormatter.ofPattern("h:mm a"))
            ?: ""

    Row(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(35.dp),
            painter = painterResource(id = R.drawable.vaccine), contentDescription = null
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
        ) {
            Text(text = endTime)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.padding(end = 8.dp),
                    imageVector = Icons.Outlined.Vaccines,
                    contentDescription = null
                )
                Text(
                    text = record.type
                )

            }
        }
    }
}
