package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.AccountAvatar
import com.ebony.cuddlecare.ui.components.BottomNavBar
import com.ebony.cuddlecare.ui.components.ProfileAvatar
import com.ebony.cuddlecare.ui.components.TipsCard
import com.ebony.cuddlecare.ui.components.TopBar
import com.ebony.cuddlecare.ui.documents.Baby


data class NavigationItem(
    val iconDrawableId: Int? = null,
    val icon: ImageVector? = null,
    val title: String,
    val destination: Screen,
)


val screens = listOf(
    NavigationItem(
        iconDrawableId = R.drawable.bf,
        title = "Breastfeeding",
        destination = Screen.BreastfeedingScreen
    ),
    NavigationItem(
        iconDrawableId = R.drawable.bottle,
        title = "Bottle",
        destination = Screen.Bottle
    ),
    NavigationItem(
        iconDrawableId = R.drawable.diaper,
        title = "Diaper",
        destination = Screen.Diaper
    ),
    NavigationItem(
        iconDrawableId = R.drawable.crib,
        title = "Sleeping",
        destination = Screen.SleepingScreen
    ),
    NavigationItem(
        iconDrawableId = R.drawable.medicine,
        title = "Medication",
        destination = Screen.MedicationScreen
    ),
    NavigationItem(
        iconDrawableId = R.drawable.vaccine,
        title = "Vaccination",
        destination = Screen.VaccineScreen
    ),
    NavigationItem(
        iconDrawableId = R.drawable.doc,
        title = "Doctor Visit",
        destination = Screen.VaccineScreen
    ),
    NavigationItem(
        iconDrawableId = R.drawable.potty,
        title = "Potty",
        destination = Screen.VaccineScreen
    ),
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
data class SummaryItem(
    val img : ImageVector,
    val time : String,


)

@Composable
fun HomeScreen(
    onNotificationClick: () -> Unit = {},
    onTopNavigation: (String) -> Unit,
    babies: List<Baby>,
    setActiveBaby: (String) -> Unit,
    activeBaby: Baby?
) {
    var isTipsCardVisible by remember { mutableStateOf(true) }
    Scaffold(
        topBar = {
            TopBar(
                onNotificationClick = onNotificationClick,
                babies = babies,
                setActiveBaby = setActiveBaby,
                activeBaby = activeBaby

            )
        },
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
            Row(modifier = Modifier.padding(top=16.dp, start = 8.dp)){

                Text(text = "Today, Sat, 23 Mar")
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(color = Color.White)
                    .fillMaxWidth()
            ) {


                Row (modifier = Modifier.padding(16.dp)){
                    Image(
                        modifier = Modifier
                            .size(25.dp),
                        painter = painterResource(id = R.drawable.bf),
                        contentDescription = null
                    )
                    Text(text = " 1")
                    Row(modifier = Modifier.padding(start = 8.dp)) {


                        Image(
                            modifier = Modifier.padding(end = 8.dp),
                            imageVector = Icons.Outlined.Timelapse, contentDescription = null
                        )
                        Text(text = "1min")
                        Text(text = " Left: <1 min")
                        Text(text = " Right: <1 min")
                    }
                }
                HorizontalDivider()
                Row (modifier = Modifier.padding(16.dp)){
                    Image(
                        modifier = Modifier
                            .size(25.dp),
                        painter = painterResource(id = R.drawable.bottle),
                        contentDescription = null
                    )
                    Text(text = " 1")
                    Row(modifier = Modifier.padding(start = 8.dp)) {
                        Image(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(25.dp),
                            painter = painterResource(id = R.drawable.cup), contentDescription = null
                        )
                        Text(text = "60ml")

                    }
                }
                HorizontalDivider()
                Row (modifier = Modifier.padding(16.dp)){
                    Image(
                        modifier = Modifier
                            .size(25.dp),
                        painter = painterResource(id = R.drawable.diaper),
                        contentDescription = null
                    )
                    Text(text = " 1")
                    Row(modifier = Modifier.padding(start = 8.dp)) {
                        Image(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(20.dp),
                            painter = painterResource(id = R.drawable.poop), contentDescription = null
                        )
                        Text(text = "1  ")
                        Image(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(23.dp),
                        painter = painterResource(id = R.drawable.pee), contentDescription = null
                        )
                        Text(text = "1")

                    }
                }
            }
            Column (modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(color = Color.White, shape = RoundedCornerShape(10)),
                verticalArrangement = Arrangement.Center
                )
            {
                Row (modifier = Modifier .padding(start=16.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Image(modifier = Modifier.size(35.dp),
                        painter = painterResource(id = R.drawable.potty), contentDescription =null )
                    Column (modifier = Modifier.padding(16.dp),
                        ) {
                        Text(text = "4:33 PM")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(modifier = Modifier
                                .size(30.dp)
                                .padding(end = 8.dp),
                                painter = painterResource(id = R.drawable.pee),
                                contentDescription = null
                            )
                            Text(text = "Pee")
                            Image(modifier = Modifier.padding(start = 8.dp, end =8.dp),
                                painter = painterResource(id = R.drawable.poop),
                                contentDescription = null
                            )
                            Text(text = "Poo")
                        }

                    }
                    Row(modifier = Modifier
                        .padding(end = 16.dp)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End) {
                        AccountAvatar(id = "iamredemmed", firstName = "Adanwa",
                            font = 13.sp,
                            radius = 45f)
                    }
                }
                HorizontalDivider()

                Row (modifier = Modifier .padding(start =16.dp, top=16.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Image(modifier = Modifier.size(35.dp),
                        painter = painterResource(id = R.drawable.bf), contentDescription =null )
                    Column (modifier = Modifier.padding(start = 16.dp),
                    ) {
                        Text(text = "4:29 PM - 4:31 PM")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(modifier = Modifier.padding(end = 8.dp),
                                imageVector = Icons.Outlined.Timelapse,
                                contentDescription = null
                            )
                            Text(text = "1min 14s Left: 46s Right: 27s")

                        }

                    }
                    Row(modifier = Modifier
                        .padding(end = 16.dp)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End) {
                        AccountAvatar(id = "iamredemmed", firstName = "Adanwa",
                            font = 13.sp,
                            radius = 45f)
                    }
                }

            }

        }
    }





}





