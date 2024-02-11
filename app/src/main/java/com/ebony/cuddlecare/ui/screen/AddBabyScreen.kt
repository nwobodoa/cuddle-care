package com.ebony.cuddlecare.ui.screen

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.ToggableButton

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
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)

                ) {

                GenderSelector()
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var text by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = text,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { text = it },
                        shape = CircleShape,
                        label = { Text("Baby Name") })
                }
                Row(
                    Modifier.fillMaxWidth(),
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

                Row(
                    Modifier.fillMaxWidth(),
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

                ) {
                    SaveButton()
                }
            }
        }
    }

}


@Composable
@Preview(showBackground = true)
fun GenderSelector() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        var isEnabled by remember { mutableStateOf(true) }


        ToggableButton(
            enabled = isEnabled,
            modifier = Modifier.weight(1f),
            onClick = { isEnabled = true }) {
            Text(text = "Boy")
        }
        ToggableButton(
            enabled = !isEnabled,
            modifier = Modifier.weight(1f),
            onClick = { isEnabled =false }) {
            Text(text = "Girl")
        }

    }
}


@Composable
fun SaveButton() {
        Button(
            onClick = {
                //TODO(code to navigate to home screen)

            }, modifier = Modifier
                .height(70.dp)
                .fillMaxWidth()


        ) {
            Text("save", fontSize = 28.sp)
        }
    }



