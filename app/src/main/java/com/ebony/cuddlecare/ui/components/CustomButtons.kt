package com.ebony.cuddlecare.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ToggableButton(
    enabled: Boolean,
    modifier: Modifier,
    onClick: () -> Unit,
    content: @Composable() (RowScope.() -> Unit)
) {
    val colors =
        if (enabled) ButtonDefaults.buttonColors() else ButtonDefaults.buttonColors(Color.LightGray)
    Button(
        onClick = onClick,
        content = content,
        modifier = modifier,
        colors = colors
    )
}