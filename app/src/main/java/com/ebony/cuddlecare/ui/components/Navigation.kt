package com.ebony.cuddlecare.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Diversity3
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PermIdentity
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.screen.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .background(colorResource(id = R.color.backcolor))
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    title,
                    color = colorResource(id = R.color.orange),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
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

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
    val destination: Screen
)

val navItems = listOf(
    BottomNavigationItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNews = false,
        destination = Screen.HomeScreen

    ),
    BottomNavigationItem(
        title = "Community",
        selectedIcon = Icons.Filled.Diversity3,
        unselectedIcon = Icons.Outlined.Diversity3,
        hasNews = false,
        badgeCount = 10,
        destination = Screen.CommunityScreen
    ),
    BottomNavigationItem(
        title = "Statistics",
        selectedIcon = Icons.Filled.Analytics,
        unselectedIcon = Icons.Outlined.Analytics,
        hasNews = false,
        destination = Screen.Statistics
    ),
    BottomNavigationItem(
        title = "Account",
        selectedIcon = Icons.Filled.PermIdentity,
        unselectedIcon = Icons.Outlined.PermIdentity,
        hasNews = true,
        destination = Screen.Profile

    )
)

@Composable
fun BottomNavBar(navigate: (String) -> Unit) {
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }




    NavigationBar {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    //Icon(item.icon!!, contentDescription = item.title)
                    BadgedBox(badge = {
                        if (item.badgeCount != null) {
                            Badge {
                                Text(text = item.badgeCount.toString())
                            }
                        } else if (item.hasNews) {
                            Badge()
                        }
                    }) {
                        Icon(
                            imageVector = if (index == selectedItemIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    }
                },
                label = { Text(item.title) },
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    navigate(item.destination.name)
                }
            )
        }

    }
}

