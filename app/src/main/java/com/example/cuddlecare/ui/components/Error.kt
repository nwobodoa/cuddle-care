package com.example.cuddlecare.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(error: String?, onClick: (String?) -> Unit){
    if (error != null){
        AlertDialog(
            onDismissRequest = { onClick(null) },
            confirmButton = {
                TextButton(onClick = { onClick(null) }, content = { Text("OK") })
            },
            title = {Text("Error", style = MaterialTheme.typography.titleMedium) },
            text = {Text(error) }
        )
    }
}