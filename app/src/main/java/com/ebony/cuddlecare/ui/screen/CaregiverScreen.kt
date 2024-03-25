package com.ebony.cuddlecare.ui.screen

import CareGiver
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.ebony.cuddlecare.ui.documents.Baby

@Composable
fun Caregivers(onNavigateBack: () -> Unit = {}, babyToUpdate: Baby?) {
//    TODO Handle this properly and this should never happen
    if (babyToUpdate == null) return
    Column {
        MTopBar(onNavigateBack = onNavigateBack, activeBaby = babyToUpdate)
        HeaderText(text = "Caregivers")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(color = colorResource(id = R.color.backcolor)),
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {

            Text(text = "Primary caregiver", fontSize = 18.sp)
            LazyColumn {
                items(babyToUpdate.primaryCareGivers.count()) {
                    CareGiverRow(careGiver = babyToUpdate.primaryCareGivers[it])
                }
            }
            Text(text = "Caregivers", fontSize = 18.sp)
            LazyColumn {
                items(babyToUpdate.careGivers.count()) {
                    CareGiverRow(careGiver = babyToUpdate.careGivers[it], showClose = true)
                }
            }
        }
    }
}

@Composable
fun CareGiverRow(careGiver:CareGiver, showClose:Boolean=false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column (modifier = Modifier
            .padding(16.dp)){

            AccountAvatar(id = careGiver.uuid, firstName = careGiver.firstname, radius = 75f)
        }
        Column {
            Text(text = "${careGiver.firstname} ${careGiver.lastname}" )
            Text(text = careGiver.email)
        }
        if(showClose) {
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