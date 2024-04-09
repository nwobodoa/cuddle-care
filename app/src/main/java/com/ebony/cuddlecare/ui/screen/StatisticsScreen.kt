package com.ebony.cuddlecare.ui.screen

import android.graphics.Typeface
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.components.Legends
import co.yml.charts.common.extensions.getMaxElementInYAxis
import co.yml.charts.common.model.LegendLabel
import co.yml.charts.common.model.LegendsConfig
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.StackedBarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarPlotData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.GroupBar
import co.yml.charts.ui.barchart.models.GroupBarChartData
import co.yml.charts.ui.barchart.models.SelectionHighlightData
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.DateInput
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.documents.BottleFeedingRecord
import com.ebony.cuddlecare.ui.documents.DiaperRecord
import com.ebony.cuddlecare.ui.documents.DiaperSoilType
import com.ebony.cuddlecare.ui.viewmodel.BottleFeedViewModel
import com.ebony.cuddlecare.ui.viewmodel.BreastFeedingRecord
import com.ebony.cuddlecare.ui.viewmodel.BreastfeedingViewModel
import com.ebony.cuddlecare.ui.viewmodel.DiaperViewModel
import com.ebony.cuddlecare.ui.viewmodel.SleepRecord
import com.ebony.cuddlecare.ui.viewmodel.SleepingViewModel
import com.ebony.cuddlecare.ui.viewmodel.StatsViewModel
import com.ebony.cuddlecare.util.epochSecondsToLocalDateTime
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.max


val pieChartConfig =
    PieChartConfig(
        labelVisible = true,
        strokeWidth = 270f,
        labelColor = Color.Black,
        activeSliceAlpha = .9f,
        isEllipsizeEnabled = true,
        labelTypeface = Typeface.defaultFromStyle(Typeface.BOLD),
        isAnimationEnable = true,
        chartPadding = 25,
        labelFontSize = 42.sp,
    )

private val formatter = DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH)


@Composable
fun StatisticsScreen(
    onNotificationClick: () -> Unit = {},
    activeBaby: Baby?,
    innerPadding: PaddingValues,
    statsViewModel: StatsViewModel = viewModel(),
    bottleFeedViewModel: BottleFeedViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    diaperViewModel: DiaperViewModel = viewModel(),
    breastFeedViewModel: BreastfeedingViewModel = viewModel(),
    sleepingViewModel: SleepingViewModel = viewModel()
) {

    val scrollState = rememberScrollState()

    val statsUIState by statsViewModel.statsUIState.collectAsState()
    val bottleFeedingUIState by bottleFeedViewModel.bottleFeedingUIState.collectAsState()
    val diaperUIState by diaperViewModel.diaperUIState.collectAsState()
    val breastfeedingUIState by breastFeedViewModel.breastfeedingUIState.collectAsState()
    val sleepUiState by sleepingViewModel.sleepingUIState.collectAsState()


    LaunchedEffect(key1 = activeBaby, key2 = statsUIState.startDate, key3 = statsUIState.endDate) {
        bottleFeedViewModel.fetchRangeRecords(
            activeBaby,
            statsUIState.startDate,
            statsUIState.endDate
        )

        diaperViewModel.fetchRangeRecords(
            activeBaby,
            statsUIState.startDate,
            statsUIState.endDate
        )

        breastFeedViewModel.fetchRangeRecords(
            activeBaby,
            statsUIState.startDate,
            statsUIState.endDate
        )
        sleepingViewModel.fetchRangeRecords(
            activeBaby,
            statsUIState.startDate,
            statsUIState.endDate
        )
    }


    Column(
        modifier = Modifier
            .verticalScroll(state = scrollState)
            .padding(innerPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ActivityMenuRow(
            onTopNavigation = { navController.navigate(it) },
            statsScreen = true
        )

        RangePicker(
            startDate = statsUIState.startDate,
            endDate = statsUIState.endDate,
            startDateExpanded = statsUIState.startDateExpanded,
            endDateExpanded = statsUIState.endDateExpanded,
            setEndDate = statsViewModel::setEndDate,
            setStartDate = statsViewModel::setStartDate,
            toggleEndDate = statsViewModel::toggleEndDateExpanded,
            toggleStartDate = statsViewModel::toggleStartDateExpanded
        )


        NavHost(navController = navController, startDestination = Screen.Bottle.name) {
            composable(Screen.Bottle.name) {
                BottleFeedingStats(
                    bottleFeedingUIState.bottleFeedingRecords,
                    statsUIState.startDate,
                    statsUIState.endDate
                )
            }
            composable(Screen.Diaper.name) {
                DiaperStats(
                    diaperRecords = diaperUIState.diaperRecords,
                    statsUIState.startDate,
                    statsUIState.endDate
                )
            }
            composable(Screen.BreastfeedingScreen.name) {
                BreastFeedingStats(
                    breastFeedingRecords = breastfeedingUIState.breastfeedingRecords,
                    statsUIState.startDate,
                    statsUIState.endDate
                )
            }
            composable(Screen.SleepingScreen.name) {
                SleepStats(
                    startDate = statsUIState.startDate,
                    endDate = statsUIState.endDate,
                    sleepRecords = sleepUiState.sleepRecords
                )
            }
        }
    }
}


@Composable
private fun RangePicker(
    startDate: LocalDate,
    endDate: LocalDate,
    setStartDate: (Long) -> Unit,
    setEndDate: (Long) -> Unit,
    startDateExpanded: Boolean,
    toggleStartDate: () -> Unit,
    endDateExpanded: Boolean,
    toggleEndDate: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextButton(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.textButtonColors(
                containerColor = colorResource(id = R.color.backcolor),
                contentColor = Color.Black
            )
        ) {
            DateInput(
                toggleDatePicker = toggleStartDate,
                isTimeExpanded = startDateExpanded,
                selectedDate = startDate,
                setSelectedDate = setStartDate
            )
        }
        Text(text = " - ", fontSize = 18.sp)
        TextButton(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.textButtonColors(
                containerColor = colorResource(id = R.color.backcolor),
                contentColor = Color.Black
            )
        ) {
            DateInput(
                toggleDatePicker = toggleEndDate,
                isTimeExpanded = endDateExpanded,
                selectedDate = endDate,
                setSelectedDate = setEndDate
            )
        }
    }
}

fun countSoilType(diaperRecords: List<DiaperRecord>): Triple<Int, Int, Int> {
    val wetCountOnly =
        diaperRecords.count { it.soilState.contains(DiaperSoilType.WET) && it.soilState.size == 1 }
    val dirtyCountOnly =
        diaperRecords.count { it.soilState.contains(DiaperSoilType.DIRTY) && it.soilState.size == 1 }
    val wetAndDirty = diaperRecords.count {
        it.soilState.contains(DiaperSoilType.DIRTY) && it.soilState.contains(DiaperSoilType.WET)
    }
    return Triple(wetCountOnly, dirtyCountOnly, wetAndDirty)
}

fun countBreastSideSession(breastFeedingRecords: List<BreastFeedingRecord>): Pair<Int, Int> {
    val left = breastFeedingRecords.count { (it.leftBreast?.activeSeconds ?: 0) > 0 }
    val right = breastFeedingRecords.count { (it.rightBreast?.activeSeconds ?: 0) > 0 }
    return Pair(left, right)

}

fun diaperRecordsToPieData(diaperRecords: List<DiaperRecord>): Triple<Float, Float, Float>? {
    if (diaperRecords.isEmpty()) return null
    val (wetCountOnly, dirtyCountOnly, wetAndDirty) = countSoilType(diaperRecords)
    val total = (wetCountOnly + dirtyCountOnly + wetAndDirty).toFloat()
    return Triple(wetCountOnly / total, dirtyCountOnly / total, wetAndDirty / total)
}


fun toBarData(value: Int, index: Int, label: String): BarData {
    return BarData(
        Point(index.toFloat(), value.toFloat()),
        label = label
    )
}


fun diaperRecordToGroupBarData(
    diaperRecords: List<DiaperRecord>,
    startDate: LocalDate,
    endDate: LocalDate
): List<GroupBar> {
    val dateByRecord =
        diaperRecords.groupBy { epochSecondsToLocalDateTime(it.timestamp)!!.toLocalDate() }
    val fullRangeByRecord =
        minDateRange(startDate, endDate).associateWith { emptyList<DiaperRecord>() } + dateByRecord

    return fullRangeByRecord
        .mapValues { countSoilType(it.value) }.toList()
        .sortedBy { it.first }
        .mapIndexed { idx, (date, values) ->
            val label = date.format(formatter)
            val barData = values.toList().map { toBarData(it, idx, label) }
            GroupBar(label, barData)
        }
}

fun breastFeedingRecordToGroupBarData(
    breastFeedingRecords: List<BreastFeedingRecord>,
    startDate: LocalDate,
    endDate: LocalDate
): List<GroupBar> {
    val dateByRecord =
        breastFeedingRecords.groupBy { epochSecondsToLocalDateTime(it.endTime!!)!!.toLocalDate() }
    val fullRangeByRecord =
        minDateRange(
            startDate,
            endDate
        ).associateWith { emptyList<BreastFeedingRecord>() } + dateByRecord

    return fullRangeByRecord
        .mapValues {
            countBreastSideSession(it.value)
        }.toList()
        .sortedBy { it.first }
        .mapIndexed { idx, (date, values) ->
            val label = date.format(formatter)
            val barData = values.toList().map { toBarData(it, idx, label) }
            GroupBar(label, barData)
        }

}

@Composable
fun DiaperStats(diaperRecords: List<DiaperRecord>, startDate: LocalDate, endDate: LocalDate) {
    if (diaperRecords.isEmpty()) {
        Text(text = "No data")
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        DiaperPieChart(diaperRecords)
        DiaperStackedBarChart(diaperRecords, startDate, endDate)
    }
}

@Composable
private fun DiaperStackedBarChart(
    diaperRecords: List<DiaperRecord>,
    startDate: LocalDate,
    endDate: LocalDate
) {
    if (diaperRecords.isEmpty()) {
        Text(text = "No data")
        return
    }
    val legendsConfig = LegendsConfig(
        legendLabelList = getDiaperLegendsLabelData(),
        gridColumnCount = 3
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ChatTitle(text = "Frequency of Each Soil type per day")
        BarChart(diaperRecordToGroupBarData(diaperRecords, startDate, endDate), legendsConfig)
    }
}

@Composable
private fun BarChart(groupBarData: List<GroupBar>, legendsConfig: LegendsConfig) {
    val listSize = groupBarData.size
    val yStepSize = 10

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(listSize - 1)
        .startDrawPadding(48.dp)
        .labelData { groupBarData[it].label }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(yStepSize)
        .labelAndAxisLinePadding(20.dp)
        .axisOffset(20.dp)
        .labelData { index ->
            val valueList = mutableListOf<Float>()
            groupBarData.map { groupBar ->
                var yMax = 0f
                groupBar.barList.forEach {
                    yMax += it.point.y
                }
                valueList.add(yMax)
            }
            val maxElementInYAxis = getMaxElementInYAxis(valueList.maxOrNull() ?: 0f, yStepSize)

            (index * (maxElementInYAxis / yStepSize)).toString()
        }
        .topPadding(36.dp)
        .build()

    val colorList = listOf(
        colorResource(id = R.color.orange),
        colorResource(id = R.color.myRed),
        colorResource(id = R.color.purple_700)
    )


    val groupBarPlotData = BarPlotData(
        groupBarList = groupBarData,
        barStyle = BarStyle(
            barWidth = 35.dp,
            selectionHighlightData = SelectionHighlightData(
                isHighlightFullBar = false,
                groupBarPopUpLabel = { name, value ->
                    "Name : C$name Value : ${String.format("%.2f", value)}"
                }
            )
        ),
        barColorPaletteList = colorList
    )
    val groupBarChartData = GroupBarChartData(
        barPlotData = groupBarPlotData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        paddingBetweenStackedBars = 0.dp,
        drawBar = { drawScope, barChartData, barStyle, drawOffset, height, barIndex ->
            with(drawScope) {
                drawRect(
                    color = colorList[barIndex],
                    topLeft = drawOffset,
                    size = Size(barStyle.barWidth.toPx(), height),
                    style = barStyle.barDrawStyle,
                    blendMode = barStyle.barBlendMode
                )
            }
        }
    )
    Column(
        Modifier
            .background(Color.White, RoundedCornerShape(10))
            .height(500.dp)
    ) {
        StackedBarChart(
            modifier = Modifier
                .height(400.dp),
            groupBarChartData = groupBarChartData
        )
        Legends(
            legendsConfig = legendsConfig
        )
    }
}

@Composable
private fun DiaperPieChart(
    diaperRecords: List<DiaperRecord>
) {
    val pieData = diaperRecordsToPieData(diaperRecords)
    if (pieData == null) {
        Text(text = "No Data")
        return
    }
    val (wet, dirty, wetDirty) = pieData
    val data = PieChartData(
        slices = listOf(
            PieChartData.Slice("Dirty", dirty, Color(0xFFEC9F05)),
            PieChartData.Slice("Wet", wet, Color(0xFF5F0A87)),
            PieChartData.Slice("Dirty and Wet", wetDirty, Color(0xFF58C2AB)),

            ),
        plotType = PlotType.Donut
    )
    Column {
        ChatTitle(text = "Diaper Soil Type")
        PieChart(data)
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun PieChart(
    data: PieChartData
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .background(Color.White, RoundedCornerShape(10))
            .fillMaxWidth()
            .height(500.dp)
    ) {
        Legends(legendsConfig = DataUtils.getLegendsConfigFromPieChartData(pieChartData = data, 3))
        DonutPieChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            data,
            pieChartConfig
        ) { slice ->
            Toast.makeText(context, slice.label, Toast.LENGTH_SHORT).show()
        }
    }
}


@Composable
fun BreastFeedingStats(
    breastFeedingRecords: List<BreastFeedingRecord>,
    startDate: LocalDate,
    endDate: LocalDate
) {
    if (breastFeedingRecords.isEmpty()) {
        Text(text = "No data")
        return
    }
    Column {
        Column {
            ChatTitle(text = "Left breast session vs Right Breast Sessions in period")
            BreastfeedingPieChart(breastFeedingRecords)
        }
        Column {
            ChatTitle(text = "Left breast sessions vs Right Breast Sessions per day")
            BreastfeedingBarChart(breastFeedingRecords, startDate, endDate)
        }
    }
}

@Composable
private fun BreastfeedingBarChart(
    breastFeedingRecords: List<BreastFeedingRecord>,
    startDate: LocalDate,
    endDate: LocalDate
) {
    val groupBarData = breastFeedingRecordToGroupBarData(breastFeedingRecords, startDate, endDate)
    val legendsConfig = LegendsConfig(
        legendLabelList = breastfeedingLegendsLabelData(),
        gridColumnCount = 2
    )
    BarChart(groupBarData = groupBarData, legendsConfig = legendsConfig)
}

@Composable
private fun BreastfeedingPieChart(breastFeedingRecords: List<BreastFeedingRecord>) {
    if (breastFeedingRecords.isEmpty()) {
        Text(text = "No data")
        return
    }

    val leftSessions = breastFeedingRecords.count { it.leftBreast != null }
    val rightSessions = breastFeedingRecords.count { it.rightBreast != null }
    val total = leftSessions + rightSessions

    val data = PieChartData(
        slices = listOf(
            PieChartData.Slice("Left Breast", leftSessions / total.toFloat(), Color(0xFF5F0A87)),
            PieChartData.Slice("Right Breast", rightSessions / total.toFloat(), Color(0xFF20BF55)),
        ),
        plotType = PlotType.Donut
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    ) {
        PieChart(data = data)
    }
}


@Composable
fun SleepStats(startDate: LocalDate, endDate: LocalDate, sleepRecords: List<SleepRecord>) {
    if (sleepRecords.isEmpty()) {
        Text(text = "No data")
        return
    }

    val startDateTime = LocalDateTime.of(startDate, LocalTime.MIDNIGHT)
    val endDateTime = LocalDateTime.of(endDate, LocalTime.MIDNIGHT)
    val totalDuration = Duration.between(startDateTime, endDateTime).seconds
    val sleepTime = sleepRecords.sumOf { it.endTimeEpochSecs - it.startTimeEpochSecs }
    val awakeTime = totalDuration - sleepTime

    val data = PieChartData(
        slices = listOf(
            PieChartData.Slice(
                "Total Hours Awake",
                awakeTime / totalDuration.toFloat(),
                Color(0xFF5F0A87)
            ),
            PieChartData.Slice(
                "Total Hours Asleep",
                sleepTime / totalDuration.toFloat(),
                Color(0xFF20BF55)
            ),

            ),
        plotType = PlotType.Donut
    )
    Column {
        ChatTitle(text = "Awake Time vs Sleep Time")
        PieChart(data = data)
    }

}

fun minDateRange(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
    val days = max(ChronoUnit.DAYS.between(startDate, endDate), 7)
    return (0..days).map { startDate.plusDays(it) }
}


fun diaperToBarChatData(
    bottleFeedingRecords: List<BottleFeedingRecord>,
    startDate: LocalDate,
    endDate: LocalDate
): List<BarData> {

    val summary = bottleFeedingRecords
        .groupBy { epochSecondsToLocalDateTime(it.timestamp)!!.toLocalDate() }
        .mapValues { it.value.sumOf { c -> c.quantityMl } }

    val rangeSummary = minDateRange(startDate, endDate).associateWith { 0 } + summary
    return rangeSummary
        .map { Pair(it.key, it.value) }
        .sortedBy { it.first }
        .mapIndexed { idx, pair ->
            val point = Point(idx.toFloat(), pair.second.toFloat())
            val label = pair.first.format(formatter)
            BarData(point = point, label = label)
        }
}


@Composable
fun BottleFeedingStats(
    bottleFeedingRecords: List<BottleFeedingRecord>,
    startDate: LocalDate,
    endDate: LocalDate
) {
    if (bottleFeedingRecords.isEmpty()) {
        Text(text = "No data")
        return
    }
    val maxRange = 100
    val barData = diaperToBarChatData(bottleFeedingRecords, startDate, endDate)
    val yStepSize = 10

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(barData.size - 1)
        .bottomPadding(40.dp)
        .axisLabelAngle(20f)
        .startDrawPadding(48.dp)
        .labelData { index -> barData[index].label }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(yStepSize)
        .labelAndAxisLinePadding(20.dp)
        .axisOffset(20.dp)
        .labelData { index -> (index * (maxRange / yStepSize)).toString() }
        .build()

    val barChartData = BarChartData(
        chartData = barData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        barStyle = BarStyle(paddingBetweenBars = 20.dp,
            barWidth = 35.dp,
            isGradientEnabled = true,
            selectionHighlightData = SelectionHighlightData(
                highlightBarColor = Color.Red,
                highlightTextBackgroundColor = Color.Green,
                popUpLabel = { _, y -> " Ml : $y " }
            )),
        showYAxis = true,
        showXAxis = true,
        horizontalExtraSpace = 20.dp
    )

    Column {
        ChatTitle(text = "Quantity of Feeds Per Day (ml)")
        BarChart(
            modifier = Modifier
                .padding(top = 32.dp)
                .height(350.dp),
            barChartData = barChartData
        )
    }

}

@Composable
fun ChatTitle(text: String) {
    Text(text = text, style = MaterialTheme.typography.h6)
}

@Composable
fun getDiaperLegendsLabelData(): List<LegendLabel> {

    val labelList = mutableListOf<LegendLabel>()
    val colorList = listOf(
        colorResource(id = R.color.orange),
        colorResource(id = R.color.myRed),
        colorResource(id = R.color.purple_700)
    )
    val labelNames = listOf("Wet", "Dirty", "Wet and Dirty")

    for (index in colorList.indices) {
        labelList.add(
            LegendLabel(
                colorList[index],
                labelNames[index]
            )
        )
    }
    return labelList
}

@Composable
fun breastfeedingLegendsLabelData(): List<LegendLabel> {

    val labelList = mutableListOf<LegendLabel>()
    val colorList = listOf(
        colorResource(id = R.color.orange),
        colorResource(id = R.color.purple_700)
    )
    val labelNames = listOf("Left", "Right")

    for (index in colorList.indices) {
        labelList.add(
            LegendLabel(
                colorList[index],
                labelNames[index]
            )
        )
    }
    return labelList
}












