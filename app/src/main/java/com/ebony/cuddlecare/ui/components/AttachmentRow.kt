package com.ebony.cuddlecare.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ebony.cuddlecare.R

@Composable
fun AttachmentRow(value: String = "", onValueChange: (String) -> Unit = {}) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val maxLength = 3000


        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = {
                if (value.length <= maxLength) {
                    onValueChange(it)
                }
            },

            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.backcolor),
                unfocusedContainerColor = colorResource(id = R.color.backcolor),
                unfocusedIndicatorColor = Color.Gray,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(autoCorrect = true),
            label = { Text(text = "Notes") },

            )
        Text(
            text = "${value.length}/$maxLength",
            textAlign = TextAlign.End,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Attachments", modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "Gallery icon",
                modifier = Modifier.align(Alignment.CenterVertically)

            )
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Camera icon",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}