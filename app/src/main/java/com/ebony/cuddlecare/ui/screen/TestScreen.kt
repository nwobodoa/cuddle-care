package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ebony.cuddlecare.R

@Composable
fun TipsCard() {
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.tipBack),),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .size(width = 150.dp, height = 150.dp)
    ) {
        Text(
            text = "Sleep", fontWeight = FontWeight.Bold,
            modifier = Modifier
                 .padding( 8.dp),
            textAlign = TextAlign.Center,
        )
        Text(
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            text = "Always place your baby on their back to sleep. Do not put them to sleep on their side or stomach. This can cause suffocating or choking, which keeps them from breathing."
        )
        Row(
            modifier = Modifier
                .padding(end = 16.dp, start = 22.dp, bottom =8.dp, top = 8.dp)
                .align(Alignment.End),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { /*TODO*/ },colors = ButtonDefaults
                .buttonColors(containerColor = colorResource(id =R.color.tipBtn))) {
                Text(text = "Save")
            }
            Button(modifier = Modifier.padding(start = 16.dp),
                onClick = { /*TODO*/ }, colors = ButtonDefaults
                    .buttonColors(containerColor = colorResource(id =R.color.tipBtn))) {
                Text(text = "Dismiss")
            }
        }
    }


}

@Preview
@Composable
fun ScrollTab() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Fruits", "Vegetables", "Meats", "Miscellaneous")
    CustomScrollableTabRow(
        tabs = tabs,
        selectedTabIndex = selectedTabIndex,
    ) { tabIndex ->
        selectedTabIndex = tabIndex
    }
}


@Composable
fun CustomScrollableTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabClick: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        contentColor = Color.White,
        edgePadding = 0.dp,
    ) {
        tabs.forEachIndexed { tabIndex, tab ->
            Tab(
                selected = selectedTabIndex == tabIndex,
                onClick = { onTabClick(tabIndex) },
                text = { Text(text = tab) }
            )
        }
    }
}




