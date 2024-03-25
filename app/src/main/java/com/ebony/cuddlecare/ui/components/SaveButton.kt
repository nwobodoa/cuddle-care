package com.ebony.cuddlecare.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SaveButton(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 16.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .height(55.dp)
                .fillMaxWidth()
        ) {
            Text("Save", fontSize = 20.sp)
        }
    }
}