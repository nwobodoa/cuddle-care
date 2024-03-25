package com.ebony.cuddlecare.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    password: String,
    label: String = "Password",
    isError: Boolean = false,
    onChange: (String) -> Unit,
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        onValueChange = { newPassword -> onChange(newPassword) },
        modifier = modifier,
        label = { Text(label) },
        trailingIcon = {
            IconButton(onClick = { visible = !visible }, modifier = Modifier.padding(4.dp)) {
                Icon(
                    imageVector = if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (visible) "Hide Password" else "Show Password"
                )

            }
        },
        isError = isError
    )
}

@Composable
fun EmailField(
    modifier: Modifier = Modifier,
    email: String,
    isError: Boolean,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = email,
        onValueChange = { onChange(it) },
        label = { Text("email") },
        modifier = modifier,
        isError = isError
    )
}

@Composable
fun ErrorText(e: String) {
    Text(
        text = e,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.error
    )
}
