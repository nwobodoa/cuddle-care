package com.ebony.cuddlecare.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PermIdentity
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.screen.NavigationItem
import com.ebony.cuddlecare.ui.screen.Screen
import com.ebony.cuddlecare.ui.theme.backcolor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar( title: String) {
    TopAppBar(
        title = {
            Box (modifier= Modifier
                .padding(top = 32.dp)
                .background(colorResource(id = R.color.backcolor))
                .fillMaxSize(), contentAlignment = Alignment.Center){

                Text (title, color = colorResource(id = R.color.orange), fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
        },

        )
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


@Composable
fun BottomNavBar(onTopNavigation: (String) -> Unit){

    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        NavigationItem(title = "Home",icon = Icons.Default.Home,destination = Screen.HomeScreen),
        NavigationItem(title = "Community",icon = Icons.Default.Diversity3,destination = Screen.CommunityScreen),
        NavigationItem(title = "Statistics",icon = Icons.Outlined.Analytics, destination = Screen.Statistics),
        NavigationItem(title = "Account",icon = Icons.Default.PermIdentity, destination = Screen.Profile)
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon!!, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    onTopNavigation(item.destination.name)
                }
            )
        }

    }
}

