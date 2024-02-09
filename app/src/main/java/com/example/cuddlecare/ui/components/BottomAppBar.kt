package com.example.cuddlecare.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar() {

    BottomAppBar(
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(Icons.Outlined.Home, contentDescription = "Localized description")
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    Icons.Filled.Mic,
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    Icons.Filled.Image,
                    contentDescription = "Localized description",
                )
            }
        }
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { /* do something */ },
//                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
//                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
//            ) {
//                Icon(Icons.Filled.Add, "Localized description")
//            }
//        }
    )


}
