package com.example.cuddlecare.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cuddlecare.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBabyScaffold() {

    val sheetState = rememberModalBottomSheetState()
    var isOpen by rememberSaveable {
        mutableStateOf(false)
    }

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
                onClick = {
                    isOpen = true
                },
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text("Add Baby", fontSize = 30.sp)

            }
        }
    }
    if (isOpen) {
        ModalBottomSheet(sheetState = sheetState,

            //dragHandle = ,
            onDismissRequest = {
                isOpen = false
            }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),

                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    var isEnabled by remember { mutableStateOf(true) }
                    var isClicked by remember { mutableStateOf(false) }
                    Button(
                        modifier = Modifier.padding(top = 16.dp), onClick = {
                            !isEnabled

                        }, colors = ButtonDefaults.buttonColors(
                            if (isEnabled) Color.Red else Color.LightGray
                        )
                    ) {
                        Text(text = "Girl")
                    }

                    Button(
                        modifier = Modifier.padding(top = 16.dp), onClick = {
                            !isClicked

                        }, colors = ButtonDefaults.buttonColors(
                            if (isClicked) Color.Red else Color.LightGray
                        )
                    ) {
                        Text(text = "Boy")

                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var text by remember { mutableStateOf("") }
                    OutlinedTextField(colors = TextFieldDefaults.colors(
                        Color.LightGray, unfocusedLabelColor = Color.LightGray
                    ),
                        value = text,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { text = it },
                        shape = CircleShape,
                        label = { Text("Baby Name") })
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Birth Date: ")
//                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                        val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
//                        DatePicker(state = state, modifier = Modifier.padding(16.dp))
//
//                        Text(
//                            "Entered date timestamp: ${state.selectedDateMillis ?: "no input"}",
//                            modifier = Modifier.align(Alignment.CenterHorizontally)
//                        )
                    }
                }
                Row(
                    Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    var checked by remember { mutableStateOf(false) }
                    Text("Premature?")
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
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp)
                ) {
                    Button(
                        onClick = {
                            //TODO(code to navigate to home screen)

                        }, modifier = Modifier
                            .padding(bottom = 16.dp)
                            .height(70.dp)
                            .fillMaxWidth()

                    ) {
                        Text("save", fontSize = 28.sp)

                    }
                }
            }
        }

    }


