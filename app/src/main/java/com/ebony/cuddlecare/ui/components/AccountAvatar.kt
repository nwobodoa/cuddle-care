package com.ebony.cuddlecare.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AccountAvatar(
    id: String,
    firstName: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    radius:Float,
    font: TextUnit = 22.sp,
    textStyle: TextStyle = androidx.compose.material.MaterialTheme.typography.subtitle1,
) {
    Box(
        modifier.size(size), contentAlignment = Alignment.Center
    ) {
        val color = remember(firstName) {
            val name = firstName
                .uppercase()
            Color("$id / $name".toHslColor())
        }
        val initials = (firstName.take(1).uppercase())
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(SolidColor(color),radius = radius)
        }
        Text(text = initials, style = textStyle, color = Color.White, fontSize= font)
    }
}