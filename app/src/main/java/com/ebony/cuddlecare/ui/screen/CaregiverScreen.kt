package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.AccountAvatar
import com.ebony.cuddlecare.ui.components.MTopBar
import com.ebony.cuddlecare.ui.theme.backcolor

@Composable
fun Caregivers(onNavigateBack: () -> Unit = {}){
    MTopBar(onNavigateBack = onNavigateBack)
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(color = colorResource(id = R.color.backcolor))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HeaderText(text = "Caregivers")
        Text(text = "Primary caregiver")
        Row(modifier = Modifier
            .background(Color.White)){
            Row {

                AccountAvatar(id = "C", firstName = "Chuka", radius = 75f)
                Column {
                    Text(text = "Igbo Man")
                    Text(text = "cinwobi@gmail.com")
                }
            }
        }
        Text(text = "Caregivers")
        Row (modifier = Modifier
            .background(Color.White)){
            Column {

                AccountAvatar(id = "A", firstName = "Adanwa", radius = 75f)
            }
            Column {
                Text(text = "Adanwa Nwobodo")
                Text(text = "adanwobodo85@gmail.com")
            }

            Icon(imageVector = Icons.Default.Close, contentDescription =null )
        }
    }
}