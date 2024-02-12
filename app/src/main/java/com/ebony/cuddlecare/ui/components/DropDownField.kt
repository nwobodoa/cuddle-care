package com.ebony.cuddlecare.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DropDownField(
    modifier: Modifier = Modifier,
    label: String,
    value: String?,
    onClick: () -> Unit
) {
    TextField(value = value ?: "",
        onValueChange = {},
        label = { Text(label) },
        modifier = modifier
            .clickable(onClick = { onClick() }),
        readOnly = true,
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = MaterialTheme.colorScheme.inverseSurface,
        ),
        enabled = false,



    )
}
