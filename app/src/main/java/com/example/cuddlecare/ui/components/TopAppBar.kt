package com.example.cuddlecare.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cuddlecare.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.orange),
            titleContentColor = Color.Black,
        ),

        navigationIcon = {
            ProfileAvatar(
                modifier = Modifier.padding(start = 16.dp), id = "D", firstName = "David"
            )
        },

        title = {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = "David"
            )
        },
        actions = {
            //search icon
            IconButton(onClick = {

            }) {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")
            }

            // lock icon
            IconButton(onClick = {

            }) {
                Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "Lock")
            }
        }

    )
}

@Composable
fun ProfileAvatar(
    id: String,
    firstName: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
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
            drawCircle(SolidColor(color), radius = 70f, style = Stroke(width = 15f), alpha = 0.3f)
            drawCircle(SolidColor(color))
        }
        androidx.compose.material.Text(text = initials, style = textStyle, color = Color.White)
    }
}

@Composable
fun DropdownItem() {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("David") },
                onClick = { /* Handle edit! */ }
            )
            DropdownMenuItem(
                text = { Text("Nathan") },
                onClick = { /* Handle settings! */ },
            )
            DropdownMenuItem(
                text = { Text("Bella") },
                onClick = { /* Handle send feedback! */ },
                trailingIcon = { Text("F11", textAlign = TextAlign.Center) })
        }
    }
}



