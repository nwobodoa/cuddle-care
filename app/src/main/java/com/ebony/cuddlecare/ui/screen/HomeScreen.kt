package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.BottomNavBar
import com.ebony.cuddlecare.ui.components.DetailedActivityList
import com.ebony.cuddlecare.ui.components.Loading
import com.ebony.cuddlecare.ui.components.TipsCard
import com.ebony.cuddlecare.ui.components.TopBar
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.documents.BottleFeedingRecord
import com.ebony.cuddlecare.ui.documents.CareGiver
import com.ebony.cuddlecare.ui.documents.DiaperRecord
import com.ebony.cuddlecare.ui.documents.DiaperSoilType
import com.ebony.cuddlecare.ui.viewmodel.BottleFeedViewModel
import com.ebony.cuddlecare.ui.viewmodel.BreastFeedingRecord
import com.ebony.cuddlecare.ui.viewmodel.BreastfeedingViewModel
import com.ebony.cuddlecare.ui.viewmodel.DiaperViewModel
import com.ebony.cuddlecare.ui.viewmodel.MedicineViewModel
import com.ebony.cuddlecare.ui.viewmodel.SleepingViewModel
import com.ebony.cuddlecare.ui.viewmodel.VaccinationViewModel
import com.ebony.cuddlecare.util.secondsToFormattedTime
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
    return date.format(DateTimeFormatter.ofPattern("EEE, d MMM", Locale.US))
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
        destination = Screen.DoctorScreen
    )

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
fun HomeNavigableScreen(
    onNotificationClick: () -> Unit = {},
    onTopNavigation: (String) -> Unit,
    babies: List<Baby>,
    setActiveBaby: (String) -> Unit,
    activeBaby: Baby?,
    setBabyToUpdate: (Baby) -> Unit,
    onSignOut: () -> Unit,
    user: CareGiver,
    navController: NavHostController = rememberNavController(),
) {

    Scaffold(
        topBar = {
            TopBar(
                onNotificationClick = onNotificationClick,
                babies = babies,
                setActiveBaby = setActiveBaby,
                activeBaby = activeBaby
            )
        },
        bottomBar = {
            BottomNavBar(
                navigate = { dest -> navController.navigate(dest) }
            )
        },
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = Screen.HomeScreen.name) {
            composable(Screen.HomeScreen.name) {
                HomeScreen(
                    onTopNavigation = onTopNavigation,
                    innerPadding = innerPadding,
                    activeBaby = activeBaby
                )
            }

            composable(Screen.CommunityScreen.name) {
                CommunityScreen(innerPadding = innerPadding)
            }

            composable(Screen.Profile.name) {
                AccountScreen(
                    user = user,
                    babies = babies,
                    onTopNavigation = onTopNavigation,
                    setBabyToUpdate = setBabyToUpdate,
                    onSignOut = onSignOut,
                    innerPadding = innerPadding
                )
            }
            composable(Screen.Statistics.name) {
                StatisticsScreen(
                    onTopNavigation = onTopNavigation,
                    babies = babies,
                    setActiveBaby = setActiveBaby,
                    activeBaby = activeBaby,
                    innerPadding = innerPadding
                )
            }

        }
    }
}

@Composable
fun HomeScreen(
    onTopNavigation: (String) -> Unit,
    innerPadding: PaddingValues,
    activeBaby: Baby?,
    bottleFeedViewModel: BottleFeedViewModel = viewModel(),
    breastFeedingViewModel: BreastfeedingViewModel = viewModel(),
    diaperViewModel: DiaperViewModel = viewModel(),
    sleepViewModel: SleepingViewModel = viewModel(),
    medicineViewModel: MedicineViewModel = viewModel(),
    vaccinationViewModel: VaccinationViewModel = viewModel()
) {
    var isTipsCardVisible by remember { mutableStateOf(true) }
    val bottleFeedingUIState by bottleFeedViewModel.bottleFeedingUIState.collectAsState()
    val breastfeedingUIState by breastFeedingViewModel.breastfeedingUIState.collectAsState()
    val diaperUIState by diaperViewModel.diaperUIState.collectAsState()
    val sleepUIState by sleepViewModel.sleepingUIState.collectAsState()
    val medicineUIState by medicineViewModel.medicineUIState.collectAsState()
    val vaccineUIState by vaccinationViewModel.vaccineUIState.collectAsState()

    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = activeBaby) {
        if (activeBaby != null) {
            bottleFeedViewModel.fetchRecords(activeBaby, LocalDate.now())
            breastFeedingViewModel.fetchRecords(activeBaby, LocalDate.now())
            diaperViewModel.fetchRecords(activeBaby, LocalDate.now())
            sleepViewModel.fetchRecord(activeBaby, LocalDate.now())
            medicineViewModel.fetchRecord(activeBaby, LocalDate.now())
            vaccinationViewModel.fetchRecord(activeBaby, LocalDate.now())
        }
    }

    val sortableActivities =
        (breastfeedingUIState.breastfeedingRecords
                + diaperUIState.diaperRecords
                + sleepUIState.sleepRecords
                + medicineUIState.medicineRecords
                + vaccineUIState.vaccinationRecords
                )

    val loading = breastfeedingUIState.loading || diaperUIState.loading || sleepUIState.loading
            || medicineUIState.loading || vaccineUIState.loading

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(top = 8.dp)

    ) {
        ActivityMenuRow(onTopNavigation)
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                TipsCard(show = isTipsCardVisible,
                    onDismiss = { isTipsCardVisible = false })
                TodayDateDisplay()
                if (loading) {
                    Loading()
                    return
                }

                if (sortableActivities.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 250.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            imageVector = Icons.AutoMirrored.Filled.DirectionsBike,
                            contentDescription = null
                        )
                        Text(text = "No data found")
                    }
                    return
                }

                ActivityDailySummary(
                    breastfeedingUIState.breastfeedingRecords,
                    bottleFeedingUIState.bottleFeedingRecords,
                    diaperUIState.diaperRecords
                )
                DetailedActivityList(sortableActivities = sortableActivities)

            }
        }
    }



@Composable
fun ActivityMenuRow(onTopNavigation: (String) -> Unit) {
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
}

@Composable
private fun TodayDateDisplay() {
    Row(modifier = Modifier.padding(top = 16.dp, start = 8.dp)) {
        Text(text = "Today, ${formatDate(LocalDate.now())}")
    }
}

@Composable
private fun ActivityDailySummary(
    breastfeedingRecords: List<BreastFeedingRecord>,
    bottleFeedingRecords: List<BottleFeedingRecord>,
    diaperRecords: List<DiaperRecord>
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(color = Color.White)
            .fillMaxWidth()
    ) {
        BreastFeedingSummary(breastFeedingRecords = breastfeedingRecords)
        HorizontalDivider()
        BottleFeedSummary(bottleFeed = bottleFeedingRecords)
        HorizontalDivider()
        DiaperSummary(diaperRecords = diaperRecords)
    }
}

@Composable
private fun BreastFeedingSummary(breastFeedingRecords: List<BreastFeedingRecord>) {
    if (breastFeedingRecords.isEmpty()) return

    val displayText: (Long) -> String = {
        if (it < 60) {
            "<1 min"
        } else {
            secondsToFormattedTime(it)
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
private fun BottleFeedSummary(bottleFeed: List<BottleFeedingRecord>) {
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





