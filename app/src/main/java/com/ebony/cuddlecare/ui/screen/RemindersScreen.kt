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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.mTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ReminderScreen() {
    val sheetState = rememberModalBottomSheetState()
    var isOpen by rememberSaveable {
        mutableStateOf(false)
    }
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
                var checked by remember { mutableStateOf(false) }
                Text(text = "Show notification for baby 'David'")
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
//                            Icon(imageVector = Icons.Filled.Cancel,
//                                contentDesciption = "cancel")
                })
            }

            FloatingActionButton(
                onClick = {
                    isOpen = true
                }, containerColor = colorResource(id = R.color.myRed),
                contentColor = Color.White, shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
            if (isOpen) {

                ModalBottomSheet(sheetState = sheetState,
                    onDismissRequest = {
                        isOpen = false
                    }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        Text(text = "Reminder")
                        val imageModifier = Modifier.size(75.dp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column {
                                Image(
                                    modifier = imageModifier,
                                    painter = painterResource(id = R.drawable.bf),
                                    contentDescription = "breastfeeding icon"
                                )
                                Text(text = "Breastfeeding")
                            }
                            Column {

                                Image(
                                    modifier = imageModifier,
                                    painter = painterResource(id = R.drawable.bottle),
                                    contentDescription = "bottle icon"
                                )
                                Text(text = "Bottle")
                            }
                            Column {

                                Image(
                                    modifier = imageModifier,
                                    painter = painterResource(id = R.drawable.diaper),
                                    contentDescription = "diaper icon"
                                )
                                Text(text = "Diaper")
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column {

                                Image(
                                    modifier = imageModifier,
                                    painter = painterResource(id = R.drawable.crib),
                                    contentDescription = "breastfeeding icon"
                                )
                                Text(text = "Sleeping")
                            }
                            Column {

                                Image(
                                    modifier = imageModifier,
                                    painter = painterResource(id = R.drawable.medicine),
                                    contentDescription = "bottle icon"
                                )
                                Text(text = "Medication")
                            }
                            Column {

                                Image(
                                    modifier = imageModifier,
                                    painter = painterResource(id = R.drawable.vaccine),
                                    contentDescription = "breastfeeding icon"
                                )
                                Text(text = "Vaccination")
                            }
                        }

                        Button(onClick = {
                            isOpen = false
                        },modifier =Modifier.fillMaxWidth()) {
                            Text(text = "Cancel", fontSize = 20.sp)
                        }
                    }
                }
            }
        }

    }
}







