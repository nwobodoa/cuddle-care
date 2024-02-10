package com.example.cuddlecare.ui.screen

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cuddlecare.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun add_diaper_record() {
    Column(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(20.dp))
    )
    {
        Row {
            Image(
                painter = painterResource(id = R.drawable.diaper_logo),
                contentDescription = null,
                Modifier
                    .size(350.dp)
                    .padding(bottom = 32.dp, top = 32.dp),
                contentScale = ContentScale.Crop
            )
        }
        Text(text = "Diaper", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(text = "Last: 20mins ago")
        Column(modifier = Modifier.background(color = Color.White)) {
            Row {
                Icon(Icons.Default.AccessTime, contentDescription = "Timer")
                Text(text = "Time")
                Text(text = "9 Nov")
                Text(text = "2:44 AM")
            }
            Row {
                var value by remember { mutableStateOf("") }
                var mExpanded by remember { mutableStateOf(false) }
                val groups = listOf("Disposable", "Cloth")
                var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

                Icon(Icons.Outlined.Folder, contentDescription = "Folder icon")
                TextField(value = value, onValueChange = { value = it },
                    label = { Text(text = "Select group") }
                )
                //TODO add drop down menu

            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = "water drop"
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Wet")

                }
                Button(onClick = { /*TODO*/ }, modifier = Modifier.padding(start = 8.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.poop),
                        contentDescription = "Poop",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Dry")
                }
            }
        }
        Row(horizontalArrangement = Arrangement.End) {
            Text(text = "Count : 0")
            Spacer(modifier = Modifier.size(6.dp))
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Plus icon")
        }
        AttachmentRow()
        SaveButton()


    }
}

@Composable
fun AttachmentRow() {
    var inputValue by remember { mutableStateOf(TextFieldValue()) }
              TextField(
                value = inputValue,
                onValueChange = { inputValue = it },
                label = { Text(text = "Notes") },
//                  inactiveColor = Color.Gray,
//                KeyboardOptions = KeyboardOptions(
//                    capitalization = KeyboardCapitalization.None,
//                    autoCorrect = true,)
            )
        Row {
            Text(text = "Attachments")
            Icon(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "Gallery icon"
            )
            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Camera icon")
        }

    }



