package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.BottomNavBar
import com.ebony.cuddlecare.ui.components.TipsCard
import com.ebony.cuddlecare.ui.components.TopBar


data class NavigationItem(
    val iconDrawableId: Int? = null,
    val icon: ImageVector? = null,
    val title: String,
    val destination: Screen,
)


val screens = listOf(
    NavigationItem(iconDrawableId = R.drawable.bf, title = "Breastfeeding", destination = Screen.BreastfeedingScreen),
    NavigationItem(iconDrawableId = R.drawable.bottle, title = "Bottle", destination = Screen.Bottle),
    NavigationItem(iconDrawableId = R.drawable.diaper, title = "Diaper", destination = Screen.Diaper),
    NavigationItem(iconDrawableId = R.drawable.crib, title = "Sleeping", destination = Screen.SleepingScreen),
    NavigationItem(iconDrawableId = R.drawable.medicine, title = "Medication", destination = Screen.MedicationScreen),
    NavigationItem(iconDrawableId = R.drawable.vaccine, title = "Vaccination", destination = Screen.VaccineScreen),
    NavigationItem(iconDrawableId = R.drawable.doc, title = "Doctor Visit", destination = Screen.VaccineScreen),
    NavigationItem(iconDrawableId = R.drawable.potty, title = "Potty", destination = Screen.VaccineScreen),
)

@Composable
fun NavigationIcon(
    drawableId: Int,
    text: String,
    onClick: () -> Unit,
    contentDescription: String = ""
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            modifier = Modifier
                .size(70.dp)
                .padding(top = 8.dp)
                .clickable { onClick() },
            painter = painterResource(id = drawableId),
            contentDescription = contentDescription
        )
        Text(text = text, fontSize = 12.sp)
    }
}


@Composable
fun HomeScreen(onNotificationClick: () -> Unit = {},
               onTopNavigation: (String) -> Unit) {
    var isTipsCardVisible by remember { mutableStateOf(true) }
    Scaffold(
        topBar = { TopBar(onNotificationClick = onNotificationClick) },
        bottomBar = { BottomNavBar(onTopNavigation) },
        ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyRow(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(screens) { item ->
                    NavigationIcon(
                        drawableId = item.iconDrawableId!!,
                        text = item.title,
                        onClick = {
                            onTopNavigation(item.destination.name)
                        })
                }
            }
            Row(modifier = Modifier.padding(top = 8.dp)) {
                if (isTipsCardVisible) {

                    TipsCard(onDismiss = { isTipsCardVisible = false })
                }
            }
        }
    }
}



