package com.ebony.cuddlecare.ui.screen

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.BarGraph
import com.ebony.cuddlecare.ui.components.BarType
import com.ebony.cuddlecare.ui.components.BottomNavBar
import com.ebony.cuddlecare.ui.components.Chart
import com.ebony.cuddlecare.ui.components.ChartScreen
import com.ebony.cuddlecare.ui.components.StackedBarChart
import com.ebony.cuddlecare.ui.components.TopBar
import com.ebony.cuddlecare.ui.components.stackedBarChartInputs
import com.ebony.cuddlecare.ui.documents.Baby
import kotlin.math.min

@Composable
fun StatisticsScreen(
    onNotificationClick: () -> Unit = {},
    onTopNavigation: (String) -> Unit,
    babies: List<Baby>,
    setActiveBaby: (String) -> Unit,
    activeBaby: Baby?
) {
    Scaffold(
        topBar = {
            TopBar(
                onNotificationClick = {},
                babies = babies,
                setActiveBaby = setActiveBaby,
                activeBaby = activeBaby

            )
        },
        bottomBar = { BottomNavBar(onTopNavigation) },
    ) { innerPadding ->
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
                Text(text = "24 Mar - 30 Mar")
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


            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(300.dp)
                .background(Color.White, shape = RoundedCornerShape(10.dp)))
            {

                Row(modifier = Modifier.padding(top = 16.dp)) {
                    Text(
                        text = "Detailed",
                        modifier = Modifier.padding(bottom = 16.dp, start = 16.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(300.dp)
                        .background(Color.White, shape = RoundedCornerShape(10.dp)),
                    verticalArrangement = Arrangement.Center
                ) {

                    // Preview with sample data
                    PieChart(
                        data = mapOf(
                            Pair("Sample-1", 150),
                            Pair("Sample-2", 120),
                            Pair("Sample-3", 110),

                            )
                    )
                }
            }

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal=8.dp, vertical = 16.dp)
                .background(Color.White, shape = RoundedCornerShape(10.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                val datalist = mutableListOf(30, 60, 90, 50, 70)
                val floatValue = mutableListOf<Float>()
                val datesList = mutableListOf(2,3,4,5,6)
                datalist.forEachIndexed { index , value ->
                floatValue.add(index = index, element = value.toFloat()/datalist.max().toFloat())
                }
                BarGraph(
                    graphBarData = floatValue,
                    xAxisScaleData = datesList,
                    barData_ = datalist,

                    height = 300.dp,
                    roundType = BarType.TOP_CURVED,
                    barWidth = 20.dp,
                    barColor = Color.Blue,
                    barArrangement = Arrangement.SpaceEvenly
                )
            }
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal=8.dp, vertical = 16.dp)
                .background(Color.White, shape = RoundedCornerShape(10.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                val datalist = mutableListOf(30, 60, 90, 50, 70)
                val floatValue = mutableListOf<Float>()
                val datesList = mutableListOf(2, 3, 4, 5, 6)
                datalist.forEachIndexed { index, value ->
                    floatValue.add(
                        index = index,
                        element = value.toFloat() / datalist.max().toFloat()
                    )
                }
                ChartScreen(
                    title = "Stacked Bar Chart",
                    chart = {
                        StackedBarChart(
                            modifier = Modifier.padding(20.dp),
                            values = stackedBarChartInputs()
                        )
                    }
                )

            }
            }



        }
        }





    @Composable
    fun PieChart(
        data: Map<String, Int>,
        radiusOuter: Dp = 90.dp,
        chartBarWidth: Dp = 50.dp,
        animDuration: Int = 1000,
) {

        val totalSum = data.values.sum()
        val floatValue = mutableListOf<Float>()

        // To set the value of each Arc according to
        // the value given in the data, we have used a simple formula.
        // For a detailed explanation check out the Medium Article.
        // The link is in the about section and readme file of this GitHub Repository
        data.values.forEachIndexed { index, values ->
            floatValue.add(index, 360 * values.toFloat() / totalSum.toFloat())
        }

        // add the colors as per the number of data(no. of pie chart entries)
        // so that each data will get a color
        val colors = listOf(
            colorResource(id = R.color.teal_200),
            colorResource(id = R.color.teal_700),
            colorResource(id = R.color.purple_200),

        )

        var animationPlayed by remember { mutableStateOf(false) }

        var lastValue = 0f

        // it is the diameter value of the Pie
        val animateSize by animateFloatAsState(
            targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
            animationSpec = tween(
                durationMillis = animDuration,
                delayMillis = 0,
                easing = LinearOutSlowInEasing
            ), label = ""
        )

        // if you want to stabilize the Pie Chart you can use value -90f
        // 90f is used to complete 1/4 of the rotation
        val animateRotation by animateFloatAsState(
            targetValue = if (animationPlayed) 90f * 11f else 0f,
            animationSpec = tween(
                durationMillis = animDuration,
                delayMillis = 0,
                easing = LinearOutSlowInEasing
            ), label = ""
        )

        // to play the animation only once when the function is Created or Recomposed
        LaunchedEffect(key1 = true) {
            animationPlayed = true
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Pie Chart using Canvas Arc
            Box(
                modifier = Modifier.size(animateSize.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .size(radiusOuter * 2f)
                        .rotate(animateRotation)
                ) {
                    // draw each Arc for each data entry in Pie Chart
                    floatValue.forEachIndexed { index, value ->
                        drawArc(
                            color = colors[index],
                            lastValue,
                            value,
                            useCenter = false,
                            style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                        )
                        lastValue += value
                    }
                }
            }
        }
    }


