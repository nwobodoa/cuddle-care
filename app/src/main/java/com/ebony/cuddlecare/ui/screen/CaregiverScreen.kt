package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.AccountAvatar
import com.ebony.cuddlecare.ui.components.MTopBar

@Composable
fun Caregivers(onNavigateBack: () -> Unit = {}) {
    Column() {
        MTopBar(onNavigateBack = onNavigateBack)
        HeaderText(text = "Caregivers")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(color = colorResource(id = R.color.backcolor)),
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {

            Text(text = "Primary caregiver", fontSize = 18.sp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Row (modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)){

                    AccountAvatar(id = "C", firstName = "Chuka", radius = 75f)
                    Column {
                        Text(text = "Igbo Man")
                        Text(text = "cinwobi@gmail.com")
                    }
                }
            }
            Text(text = "Caregivers", fontSize = 18.sp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column (modifier = Modifier
                    .padding(16.dp)){

                    AccountAvatar(id = "A", firstName = "Adanwa", radius = 75f)
                }
                Column {
                    Text(text = "Adanwa Nwobodo")
                    Text(text = "adanwobodo85@gmail.com")
                }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center) {

                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
            }
        }
    }
}