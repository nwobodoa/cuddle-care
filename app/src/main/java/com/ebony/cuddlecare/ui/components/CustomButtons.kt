package com.ebony.cuddlecare.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.ebony.cuddlecare.R

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

@Composable
fun SwitchWithIcon(isChecked: Boolean = false, onCheck: (Boolean) -> Unit = {}) {
    Switch(
        checked = isChecked, onCheckedChange = onCheck, thumbContent = {
            Icon(
                imageVector = if (isChecked) Icons.Filled.Check else Icons.Filled.Close,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        },
        colors = SwitchDefaults.colors(
            checkedTrackColor = colorResource(id = R.color.myRed)

        )
    )
}
