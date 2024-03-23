package com.ebony.cuddlecare.ui.screen

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
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.LeadingDetailsIcon
import com.ebony.cuddlecare.ui.components.MTopBar
import com.ebony.cuddlecare.ui.components.SaveButton
import com.ebony.cuddlecare.ui.components.ToggableButton


@Preview
@Composable
fun BreastfeedingScreen(onNavigateBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.orange))
    )
    {
        MTopBar(onNavigateBack = onNavigateBack)
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(color = colorResource(id = R.color.backcolor))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ScreenMainIcon(R.drawable.breastfeeding_logo)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    LastUpdated("Breastfeeding", "Last: Never")
                }
                Row {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add icon")
                    Icon(imageVector = Icons.Default.Delete, contentDescription = " Delete icon")
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
                    modifier = Modifier
                        .fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BreastFeedingControlBtn(
                        modifier = Modifier.weight(1f),
                        breastSide = "L",
                        onClick = {/*TODO*/ })
                    BreastFeedingControlBtn(
                        modifier = Modifier.weight(1f),
                        breastSide = "R",
                        onClick = {/*TODO*/ })
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


                Row {
                    LeadingDetailsIcon(
                        title = "Duration", imageVector = Icons.Default.Timelapse,
                        contentDescription = "timeelapsed icon"
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "1 min 50 s")
                    }
                }
                Row {
                    LeadingDetailsIcon(
                        title = "Pause", imageVector = Icons.Default.Pause,
                        contentDescription = "pause icon"
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "0 s")
                    }
                }
                Row {
                    LeadingDetailsIcon(
                        title = "Started", imageVector = Icons.Default.Timer,
                        contentDescription = "timer icon"
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "9 Nov ")
                        Text(text = " 2:44 AM")
                    }
                }
                Row {
                    LeadingDetailsIcon(
                        title = "Ended", imageVector = Icons.Default.Flag,
                        contentDescription = "timer icon"
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "9 Nov ")
                        Text(text = " 2:44 AM")
                    }
                }
            }
            AttachmentRow()
            SaveButton(onClick = {})
        }
    }
}

@Composable
fun BreastFeedingControlBtn(
    modifier: Modifier = Modifier,
    breastSide: String,
    onClick: () -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        ToggableButton(
            activated = false,
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "play icon")
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = "($breastSide)Start")
        }
        Text(text = "0 s")
    }
}