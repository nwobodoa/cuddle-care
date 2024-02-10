package com.example.cuddlecare.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.PermIdentity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navigationController: NavController, title: String) {
    TopAppBar(title = { Text(title) }, navigationIcon = {
        IconButton(
            onClick = { navigationController.popBackStack() },
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
    })
}

@Composable
fun ScreenScaffold(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    topBar: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(topBar = topBar) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                horizontalAlignment = horizontalAlignment,
                verticalArrangement = verticalArrangement
            ) {
                content()

            }
        }

    }
}
class NavItem(val item : String, val itemIcon : ImageVector)

@Preview
@Composable
fun BottomNavBar(){

    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        NavItem("Home",Icons.Default.Home),
        NavItem("Community",Icons.Default.Diversity3),
        NavItem("Development",Icons.Outlined.PermIdentity),
        NavItem("Statistics",Icons.Outlined.Analytics))

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.itemIcon, contentDescription = item.item) },
                label = { Text(item.item) },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}