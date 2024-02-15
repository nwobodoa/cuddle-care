package com.ebony.cuddlecare.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ToggableButton(
    activated: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable() (RowScope.() -> Unit)
) {
    val colors =
        if (activated) ButtonDefaults.buttonColors() else ButtonDefaults.buttonColors(Color.LightGray)
    Button(
        onClick = onClick,
        content = content,
        modifier = modifier,
        colors = colors
    )
}