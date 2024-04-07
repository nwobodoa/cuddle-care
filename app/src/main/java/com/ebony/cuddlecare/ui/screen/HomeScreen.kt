package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.AccountAvatar
import com.ebony.cuddlecare.ui.components.BottomNavBar
import com.ebony.cuddlecare.ui.components.TipsCard
import com.ebony.cuddlecare.ui.components.TopBar
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.documents.BottleFeed
import com.ebony.cuddlecare.ui.documents.DiaperRecord
import com.ebony.cuddlecare.ui.documents.DiaperSoilType
import com.ebony.cuddlecare.ui.viewmodel.BottleFeedViewModel
import com.ebony.cuddlecare.ui.viewmodel.BottleFeedingUIState
import com.ebony.cuddlecare.ui.viewmodel.BreastFeedingRecord
import com.ebony.cuddlecare.ui.viewmodel.BreastfeedingUIState
import com.ebony.cuddlecare.ui.viewmodel.BreastfeedingViewModel
import com.ebony.cuddlecare.ui.viewmodel.DiaperUIState
import com.ebony.cuddlecare.ui.viewmodel.DiaperViewModel
import com.ebony.cuddlecare.util.secondsToFormattedString
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


data class NavigationItem(
    val iconDrawableId: Int? = null,
    val icon: ImageVector? = null,
    val title: String,
    val destination: Screen,
)

fun formatDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("EEE, d MMM", Locale.US) // Define the formatter pattern
    return date.format(formatter)
}

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

@Composable
fun HomeScreen(
    onNotificationClick: () -> Unit = {},
    onTopNavigation: (String) -> Unit,
    babies: List<Baby>,
    setActiveBaby: (String) -> Unit,
    activeBaby: Baby?,
    bottleFeedViewModel: BottleFeedViewModel = viewModel(),
    breastFeedingViewModel: BreastfeedingViewModel = viewModel(),
    diaperViewModel: DiaperViewModel = viewModel()
) {
    var isTipsCardVisible by remember { mutableStateOf(true) }
    val bottleFeedingUIState by bottleFeedViewModel.bottleFeedingUIState.collectAsState()
    val breastfeedingUIState by breastFeedingViewModel.breastfeedingUIState.collectAsState()
    val diaperUIState by diaperViewModel.diaperUIState.collectAsState()

    LaunchedEffect(key1 = activeBaby) {
        if (activeBaby != null) {
            bottleFeedViewModel.fetchRecords(activeBaby, LocalDate.now())
            breastFeedingViewModel.fetchRecords(activeBaby, LocalDate.now())
            diaperViewModel.fetchRecords(activeBaby, LocalDate.now())
        }
    }




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
            Row(modifier = Modifier.padding(top = 16.dp, start = 8.dp)) {
                Text(text = "Today, ${formatDate(LocalDate.now())}")
            }

            ActivityDailySummary(breastfeedingUIState, bottleFeedingUIState, diaperUIState)


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(10)),
                verticalArrangement = Arrangement.Center
            )
            {
                Row(
                    modifier = Modifier.padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(35.dp),
                        painter = painterResource(id = R.drawable.potty), contentDescription = null
                    )
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(text = "4:33 PM")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(end = 8.dp),
                                painter = painterResource(id = R.drawable.pee),
                                contentDescription = null
                            )
                            Text(text = "Pee")
                            Image(
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                                painter = painterResource(id = R.drawable.poop),
                                contentDescription = null
                            )
                            Text(text = "Poo")
                        }

                    }
                    Row(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        AccountAvatar(
                            id = "iamredemmed", firstName = "Adanwa",
                            font = 13.sp,
                            radius = 45f
                        )
                    }
                }
                HorizontalDivider()

                Row(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(35.dp),
                        painter = painterResource(id = R.drawable.bf), contentDescription = null
                    )
                    Column(
                        modifier = Modifier.padding(start = 16.dp),
                    ) {
                        Text(text = "4:29 PM - 4:31 PM")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                modifier = Modifier.padding(end = 8.dp),
                                imageVector = Icons.Outlined.Timelapse,
                                contentDescription = null
                            )
                            Text(text = "1min 14s Left: 46s Right: 27s")

                        }

                    }
                    Row(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        AccountAvatar(
                            id = "iamredemmed", firstName = "Adanwa",
                            font = 13.sp,
                            radius = 45f
                        )
                    }
                }

            }

        }
    }


}

@Composable
private fun ActivityDailySummary(
    breastfeedingUIState: BreastfeedingUIState,
    bottleFeedingUIState: BottleFeedingUIState,
    diaperUIState: DiaperUIState
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(color = Color.White)
            .fillMaxWidth()
    ) {
        BreastFeedingSummary(breastFeedingRecords = breastfeedingUIState.breastfeedingRecords)
        HorizontalDivider()
        BottleFeedSummary(bottleFeed = bottleFeedingUIState.todayBottleFeed)
        HorizontalDivider()
        DiaperSummary(diaperRecords = diaperUIState.diaperRecords)
    }
}

@Composable
private fun BreastFeedingSummary(breastFeedingRecords: List<BreastFeedingRecord>) {
    if (breastFeedingRecords.isEmpty()) return

    val displayText: (Long) -> String = {
        if (it < 60) {
            "<1 min"
        } else {
            secondsToFormattedString(it)
        }
    }

    val sumRightBreastTotalTime = breastFeedingRecords.sumOf { it.rightBreast?.activeSeconds ?: 0 }
    val sumLeftBreastTotalTime = breastFeedingRecords.sumOf { it.leftBreast?.activeSeconds ?: 0 }


    Row(modifier = Modifier.padding(16.dp)) {
        SummaryLeadIcon(count = breastFeedingRecords.size.toString(), resourceId = R.drawable.bf)
        Row(
            modifier = Modifier.padding(start = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                modifier = Modifier.padding(end = 8.dp),
                imageVector = Icons.Outlined.Timelapse, contentDescription = null
            )
            Text(text = displayText(sumLeftBreastTotalTime + sumRightBreastTotalTime))
            Text(text = "Left: ${displayText(sumLeftBreastTotalTime)}")
            Text(text = "Right: ${displayText(sumRightBreastTotalTime)}")
        }
    }
}

@Composable
private fun DiaperSummary(diaperRecords: List<DiaperRecord>) {
    if (diaperRecords.isEmpty()) return
    val wetDiapers = diaperRecords.count { it.soilState.contains(DiaperSoilType.WET) }
    val dirtyDiapers = diaperRecords.count { it.soilState.contains(DiaperSoilType.DIRTY) }
    val totalCount = wetDiapers + dirtyDiapers

    Row(modifier = Modifier.padding(16.dp)) {
        SummaryLeadIcon(count = totalCount.toString(), resourceId = R.drawable.diaper)
        Row(modifier = Modifier.padding(start = 8.dp)) {
            Image(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.dp),
                painter = painterResource(id = R.drawable.poop),
                contentDescription = null
            )

            Text(text = dirtyDiapers.toString())
            Image(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(23.dp),
                painter = painterResource(id = R.drawable.pee),
                contentDescription = null
            )
            Text(text = wetDiapers.toString())

        }
    }
}

@Composable
private fun BottleFeedSummary(bottleFeed: List<BottleFeed>) {
    if (bottleFeed.isEmpty()) return
    Row(modifier = Modifier.padding(16.dp)) {
        SummaryLeadIcon(bottleFeed.count().toString(), resourceId = R.drawable.bottle)
        Row(modifier = Modifier.padding(start = 8.dp)) {
            Image(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(25.dp),
                painter = painterResource(id = R.drawable.cup), contentDescription = null
            )
            Text(text = "${bottleFeed.sumOf { it.quantityMl }} ml")
        }
    }
}

@Composable
private fun SummaryLeadIcon(count: String, resourceId: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Image(
            modifier = Modifier.size(25.dp),
            painter = painterResource(id = resourceId),
            contentDescription = null
        )
        Text(text = count)
    }
}





