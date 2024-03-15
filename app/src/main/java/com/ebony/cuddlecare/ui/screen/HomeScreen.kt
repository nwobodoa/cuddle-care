package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.BottomNavBar
import com.ebony.cuddlecare.ui.components.TipsCard
import com.ebony.cuddlecare.ui.components.TopBar
import java.io.InputStream


data class NavigationItem(
    val iconDrawableId:Int,
    val title:String,
    val destination: Screen,
)


val screens = listOf(
    NavigationItem(R.drawable.bf,"breastfeeding", Screen.BreastfeedingScreen),
    NavigationItem(R.drawable.bottle,"Bottle",Screen.Bottle),
    NavigationItem(R.drawable.diaper,"Diaper",Screen.Diaper),
    NavigationItem(R.drawable.crib,"Screen",Screen.SleepingScreen),
    NavigationItem(R.drawable.medicine,"Medication", Screen.MedicationScreen),
    NavigationItem(R.drawable.vaccine,"Vaccination",Screen.VaccineScreen),
    NavigationItem(R.drawable.doc,"Doctor Visit",Screen.VaccineScreen),
    NavigationItem(R.drawable.potty,"potty icon",Screen.VaccineScreen),
)
@Composable
fun NavigationIcon(drawableId:Int,text:String,onClick:() -> Unit,contentDescription:String="") {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
    IconButton(onClick = onClick) {
            Image(modifier=Modifier.size(70.dp),painter = painterResource(id =drawableId), contentDescription = contentDescription)
        }
        Text(text = text, fontSize = 9.sp)
    }
}

@Composable
fun HomeScreen(onNotificationClick:() -> Unit = {} ,onTopNavigation:(String) -> Unit){
    Scaffold (
        topBar = { TopBar(onNotificationClick = onNotificationClick)},
        bottomBar={ BottomNavBar()},

    ){innerPadding ->
        Column(
             modifier = Modifier.padding(innerPadding)
        ) {
  LazyRow(modifier= Modifier.padding(start=8.dp, end = 8.dp),horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      items(screens) { item ->
          NavigationIcon(drawableId = item.iconDrawableId, text = item.title, onClick = {
              onTopNavigation(item.destination.name)
          })
      }
  }
  Row (modifier = Modifier.padding(top = 32.dp)){

      TipsCard()
  }


        }



    }


}



