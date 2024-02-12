package com.ebony.cuddlecare.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview(showBackground = true)
fun DropDownField(
    modifier: Modifier = Modifier,
    label: String = "",
    value: String? = "some-val",
    onClick: () -> Unit = {}
) {

    Text(
        modifier = modifier.clickable { onClick() }, text = value ?: "",
        textAlign = TextAlign.End
    )
}
