package com.ebony.cuddlecare.ui.screen

import android.content.Context
import android.graphics.Typeface
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.components.Legends
import co.yml.charts.common.extensions.getMaxElementInYAxis
import co.yml.charts.common.model.LegendLabel
import co.yml.charts.common.model.LegendsConfig
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.common.utils.DataUtils.getGroupBarChartData
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.StackedBarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarPlotData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.GroupBarChartData
import co.yml.charts.ui.barchart.models.SelectionHighlightData
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.documents.Baby


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StatisticsScreen(
    onNotificationClick: () -> Unit = {},
    onTopNavigation: (String) -> Unit,
    babies: List<Baby>,
    setActiveBaby: (String) -> Unit,
    activeBaby: Baby?,
    innerPadding: PaddingValues
) {
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
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(state = scrollState)
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        )
        {
            Image(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(50.dp),
                painter = painterResource(id = R.drawable.diaper), contentDescription = null
            )

            Text(text = "Diaper", fontWeight = FontWeight.Bold, fontSize = 30.sp)
            Image(imageVector = Icons.Default.ExpandMore, contentDescription = null)

        }
        Row(verticalAlignment = Alignment.CenterVertically) {

            TextButton(onClick = { /*TODO*/ },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = colorResource(id = R.color.backcolor),
                    contentColor = Color.Black
                )
            ) {
                Text(text = "24 Mar - 30 Mar", fontSize = 18.sp)
            }
            IconButton(onClick = { /*TODO*/ }) {
                Image(
                    modifier = Modifier
                        .size(35.dp),
                    imageVector = Icons.Default.CalendarMonth, contentDescription = null
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            Column(
                modifier = Modifier
                    .border(
                        width = 1.dp, color = Color.Green,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .width(150.dp)
                    .background(Color.White, shape = RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                Text(text = "Number of Times")
                Text(text = "")
                Text(text = "Day")

            }

            Column(
                modifier = Modifier
                    .width(150.dp)
                    .height(90.dp)
                    .background(Color.White, shape = RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                Text(text = "Time of Day")


            }
        }
            BottleFeedingSummary()
            //DiaperSummary(context = LocalContext.current, pieChartConfig = pieChartConfig)



    }

}





@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DiaperSummary(context: Context, pieChartConfig: PieChartConfig){
    val data = PieChartData(
        slices = listOf(
                PieChartData.Slice("Dirty", 15f, Color(0xFFEC9F05)),
                PieChartData.Slice("Wet", 30f, Color(0xFF5F0A87)),
                PieChartData.Slice("Dirty and Wet", 10f, Color(0xFF58C2AB)),

            ),
        plotType = PlotType.Donut
    )
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

    val barSize = 3
    val listSize = 10
    val groupBarData = getGroupBarChartData(listSize, 50, barSize)
    val yStepSize = 10
    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(listSize - 1)
        .startDrawPadding(48.dp)
        .labelData { index -> "C $index" }
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

    val colorList = listOf<Color>(
        colorResource(id = R.color.orange),
        colorResource(id = R.color.myRed),
        colorResource(id = R.color.purple_700)
    )

    val legendsConfig = LegendsConfig(
        legendLabelList = getDiaperLegendsLabelData(),
        gridColumnCount = 3
    )
    val groupBarPlotData = BarPlotData(
        groupBarList = groupBarData,
        barStyle = BarStyle(
            barWidth = 35.dp,
            selectionHighlightData = SelectionHighlightData(
                isHighlightFullBar = true,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BreastFeedingSummary(context: Context, pieChartConfig: PieChartConfig){
    val data = PieChartData(
        slices = listOf(
            PieChartData.Slice("Left Breast", 15f, Color(0xFF5F0A87)),
            PieChartData.Slice("Right Breast", 30f, Color(0xFF20BF55)),

        ),
        plotType = PlotType.Donut
    )

    Column(
        modifier = Modifier
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

    val barSize = 2
    val listSize = 10
    val groupBarData = getGroupBarChartData(listSize, 50, barSize)
    val yStepSize = 10
    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(listSize - 1)
        .startDrawPadding(48.dp)
        .labelData { index -> "C $index" }
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

    val legendsConfig = LegendsConfig(
        legendLabelList = getDiaperLegendsLabelData(),
        gridColumnCount = 3
    )
    val groupBarPlotData = BarPlotData(
        groupBarList = groupBarData,
        barStyle = BarStyle(
            barWidth = 35.dp,
            selectionHighlightData = SelectionHighlightData(
                isHighlightFullBar = true,
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



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SleepSummary(context: Context, pieChartConfig: PieChartConfig){
    val data = PieChartData(
        slices = listOf(
            PieChartData.Slice("Total Hours Awake", 15f, Color(0xFF5F0A87)),
            PieChartData.Slice("Total Hours Asleep", 30f, Color(0xFF20BF55)),

            ),
        plotType = PlotType.Donut
    )

    Column(
        modifier = Modifier
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

    val maxRange = 100
    val barData = DataUtils.getGradientBarChartData(50, 100)
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
                popUpLabel = { _, y -> " Value : $y " }
            )),
        showYAxis = true,
        showXAxis = true,
        horizontalExtraSpace = 20.dp
    )
    BarChart(modifier = Modifier
        .padding(top = 32.dp)
        .height(350.dp), barChartData = barChartData)
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottleFeedingSummary(){
    val maxRange = 100
    val barData = DataUtils.getGradientBarChartData(50, 100)
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
                popUpLabel = { _, y -> " Value : $y " }
            )),
        showYAxis = true,
        showXAxis = true,
        horizontalExtraSpace = 20.dp
    )
    BarChart(modifier = Modifier
        .padding(top = 32.dp)
        .height(350.dp), barChartData = barChartData)

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












