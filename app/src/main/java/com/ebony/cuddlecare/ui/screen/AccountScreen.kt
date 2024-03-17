package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ebony.cuddlecare.ui.components.AccountAvatar
import com.ebony.cuddlecare.ui.components.BottomNavBar
import com.ebony.cuddlecare.ui.components.ProfileAvatar
import com.ebony.cuddlecare.ui.components.TopBar
import com.ebony.cuddlecare.ui.theme.backcolor


@Composable
fun AccountScreen(onTopNavigation:(String) -> Unit){
    Scaffold(
        topBar = {
                HeaderText(text = "Account")

            },
        bottomBar = { BottomNavBar(onTopNavigation)}
    ) {
           Column (modifier = Modifier
               .fillMaxWidth()
               .padding(it),
                Arrangement.Center
            ) {
               Column(
                   modifier = Modifier
                       .wrapContentSize()
                       .background(color = Color.White)
                       .fillMaxWidth()
                       .padding(16.dp)
               ) {
                   Row(
                       modifier = Modifier
                           .wrapContentSize()
                           .fillMaxWidth()
                           .padding(bottom = 16.dp),
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.spacedBy(8.dp)
                   ) {
                       AccountAvatar(id = "C", firstName = "Chuka", radius = 75f)
                       Column {
                           Text(text = "Adanwa Nwobodo")
                           Text(text = "adanwanwobodo85@gmail.com")

                       }
                       Column (modifier = Modifier.fillMaxWidth(),
                           horizontalAlignment = Alignment.End){
                           Text(text = "...", fontSize = 28.sp, modifier = Modifier
                               .clickable { /*TODO*/ })
                       }

                   }
                   Divider()
                   Row(modifier = Modifier
                       .padding(top = 8.dp)
                       .fillMaxWidth(),
                       horizontalArrangement = Arrangement.SpaceBetween) {
                       Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                           verticalAlignment = Alignment.CenterVertically) {

                           Canvas(modifier = Modifier.wrapContentSize(),
                               ) {
                               drawCircle(SolidColor(Color.Green),radius = 20f)
                           }
                           Text(text = "Connected")
                       }
                       Icon(imageVector = Icons.Default.Repeat, contentDescription = null)
                   }
               }
               Row (modifier = Modifier
                   .padding(16.dp)
                   .fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceBetween){

                   Text(text = "Babies")
                   Icon(imageVector = Icons.Default.AddCircle, contentDescription = null )
               }
               Column(
                   modifier = Modifier
                       .wrapContentSize()
                       .background(color = Color.White)
                       .fillMaxWidth()
                       .padding(16.dp)
               ) {
                   Row(
                       modifier = Modifier
                           .wrapContentSize()
                           .fillMaxWidth()
                           .padding(bottom = 16.dp),
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.spacedBy(8.dp)
                   ) {
                       ProfileAvatar(id = "J", firstName = "JF")
                       Column (modifier = Modifier.padding(end = 16.dp)){
                           Text(text = "JF")
                           Text(text = "0d (week 1)")

                       }
                       Column (modifier = Modifier.fillMaxWidth(),
                           horizontalAlignment = Alignment.End){
                           Text(text = "...", fontSize = 28.sp)
                       }

                   }
                   Divider()
                   Row(modifier = Modifier
                       .padding(top = 8.dp)
                       .fillMaxWidth(),
                       horizontalArrangement = Arrangement.SpaceBetween) {
                       Row {
                           AccountAvatar(id = "A", firstName = "Adanwa",radius=50f,font=15.sp)
                           AccountAvatar(id = "C", firstName = "Chuka", radius = 50f, font= 15.sp)
                       }

                      OutlinedButton(onClick = { /*TODO*/ }) {
                          Icon(imageVector = Icons.Outlined.PeopleOutline, contentDescription = null )
                          Text(text = "Caregivers")
                          
                      }
                   }
               }


           }
    }
    }
@Composable
fun HeaderText(text:String){
    Row (
        Modifier
            .fillMaxWidth()
            .background(color = backcolor)
            .padding(16.dp)){
        Text(text = text, fontWeight = FontWeight.Bold, fontSize = 28.sp) }

}
