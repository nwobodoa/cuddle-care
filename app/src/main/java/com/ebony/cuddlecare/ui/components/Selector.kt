package com.ebony.cuddlecare.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview(showBackground = true)
fun ScrollSelect(
    modifier: Modifier = Modifier,
    items: List<Int> = listOf(1, 2, 3, 5, 6, 7),
    numberOfDisplayedItems: Int = 4,
    itemHeight: Dp = 100.dp,
    width: Dp = 200.dp,
    initialValue: Int = 0,
    onSelected: (Int) -> Unit = {}
) {
    val scrollState = rememberLazyListState(0)
    var lastSelectedIndex by remember {
        mutableIntStateOf(0)
    }
    var itemsState by remember {
        mutableStateOf(items)
    }

    LaunchedEffect(items) {
        var targetIndex = items.indexOf(initialValue) - 1
        targetIndex += ((Int.MAX_VALUE / 2) / items.size) * items.size
        itemsState = items
        lastSelectedIndex = (targetIndex % items.size) + items.size
        scrollState.animateScrollToItem(index = lastSelectedIndex)
    }

    LazyColumn(
        modifier = modifier
            .height(itemHeight * numberOfDisplayedItems),
        state = scrollState,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = scrollState
        )
    ) {
        items(count = Int.MAX_VALUE) {
            println("index $it")
            LazyItem(
                item = itemsState[it % itemsState.size],
                index = it % itemsState.size,
                numberOfDisplayedItems = numberOfDisplayedItems,
                lastSelectedIndex = lastSelectedIndex,
                totalSize = itemsState.size
            ) { index, item ->
                lastSelectedIndex = index
                onSelected(item)
            }
        }
    }
}


@Composable
fun LazyItem(
    lastSelectedIndex: Int = 0,
    index: Int = 0,
    textColor: Color = Color.LightGray,
    selectedColor: Color = Color.Black,
    item: Int = 0,
    numberOfDisplayedItems: Int = 4,
    totalSize: Int = 1,
    itemHeight: Dp = 100.dp,
    onItemSelected: (index: Int, item: Int) -> Unit,
) {
    val isSelected = lastSelectedIndex == index
    val color = if (isSelected) selectedColor else textColor
    val fontSize = if (isSelected) 50.sp else 20.sp
    val itemHalfHeight = LocalDensity.current.run { itemHeight.toPx() / 2f }


    Column(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                val y = coordinates.positionInParent().y - itemHalfHeight
                val parentHalfHeight = (itemHalfHeight * numberOfDisplayedItems)
                val isCurrentItem =
                    (y > parentHalfHeight - itemHalfHeight && y < parentHalfHeight + itemHalfHeight)

                if (isCurrentItem && lastSelectedIndex != index - 1) {
                    onItemSelected((index - 1 + totalSize) % totalSize, item)
                }
            },
        verticalArrangement = if (isSelected) Arrangement.SpaceBetween else Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        if (isSelected) {
            HorizontalDivider(color = Color.LightGray)
        }
        Text(
            text = item.toString(),
            color = color,
            fontSize = fontSize
        )
        if (isSelected) {
            HorizontalDivider(color = Color.LightGray)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun ScrollSelector(
    initialDays: Int = 0, initialHours: Int = 0, initialMin: Int = 0,
    onDaySelected: (Int) -> Unit = {},
    onHourSelected: (Int) -> Unit = {},
    onMinSelected: (Int) -> Unit = {},
    isOpen: Boolean = true,
    onDismissRequest: () -> Unit = {}
) {
    if (!isOpen) return

    ModalBottomSheet(onDismissRequest = onDismissRequest)
    {
        // Sheet content
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScrollSelect(
                modifier = Modifier.weight(1f),
                items = (0..30).toList(),
                initialValue = initialDays,
                onSelected = onDaySelected
            )
            Text(
                modifier = Modifier.weight(0.5f), text = "d",
                textAlign = TextAlign.Center, fontSize = 25.sp
            )
            ScrollSelect(
                modifier = Modifier.weight(1f),
                items = (0..23).toList(),
                initialValue = initialHours,
                onSelected = onHourSelected
            )

            Text(
                modifier = Modifier.weight(0.5f), text = "h",
                textAlign = TextAlign.Center, fontSize = 25.sp
            )
            ScrollSelect(
                modifier = Modifier.weight(1f),
                items = (0..59).toList(),
                initialValue = initialMin,
                onSelected = onMinSelected
            )
            Text(
                modifier = Modifier.weight(0.5f), text = "m",
                textAlign = TextAlign.Center,
                fontSize = 25.sp
            )

        }
    }
}