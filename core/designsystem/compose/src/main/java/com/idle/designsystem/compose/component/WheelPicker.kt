package com.idle.designsystem.compose.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.idle.compose.clickable
import com.idle.designsystem.compose.FLIP
import com.idle.designsystem.compose.foundation.CareTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> CareWheelPicker(
    items: List<T>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    initIndex: Int = 0,
    pickerMaxHeight: Dp = 150.dp,
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initIndex)
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val coroutineScope = rememberCoroutineScope()

    // Int 타입이면 padStart 적용, 아니면 기본 toString 사용
    val displayItems = listOf("") + items.map { item ->
        when (item) {
            is Int -> item.toString().padStart(2, '0')
            else -> item.toString()
        }
    } + listOf("")

    val density = LocalDensity.current
    val scrollThreshold = with(density) { 30.dp.toPx().toInt() }
    val isScrolledPastThreshold by remember { derivedStateOf { listState.firstVisibleItemScrollOffset >= scrollThreshold } }
    val currentFirstItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }

    LaunchedEffect(isScrolledPastThreshold) {
        val selectedItemIndex = if (isScrolledPastThreshold) currentFirstItemIndex + 2
        else currentFirstItemIndex + 1

        onItemSelected(displayItems[selectedItemIndex])
    }

    Box(modifier = modifier.width(52.dp)) {
        LazyColumn(
            state = listState,
            flingBehavior = snapFlingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(pickerMaxHeight),
        ) {
            itemsIndexed(items = displayItems) { idx, item ->
                val selectedItemIndex = if (isScrolledPastThreshold) currentFirstItemIndex + 2
                else currentFirstItemIndex + 1

                if (idx == selectedItemIndex) {
                    Text(
                        text = item,
                        style = CareTheme.typography.heading2,
                        color = CareTheme.colors.gray900,
                        modifier = Modifier.height(30.dp),
                    )
                } else {
                    Text(
                        text = item,
                        style = CareTheme.typography.subtitle1,
                        color = CareTheme.colors.gray100,
                        modifier = Modifier
                            .height(30.dp)
                            .clickable {
                                if (!listState.isScrollInProgress &&
                                    idx !in setOf(0, displayItems.size - 1)
                                ) {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(idx - 1)
                                    }
                                }
                            },
                    )
                }
            }
        }

        HorizontalDivider(
            thickness = 2.dp,
            color = CareTheme.colors.gray100,
            modifier = Modifier.padding(top = 44.dp)
        )

        HorizontalDivider(
            thickness = 2.dp,
            color = CareTheme.colors.gray100,
            modifier = Modifier.padding(top = 104.dp)
        )
    }
}

@Composable
private fun CareWheelPickerContent(list: List<Any>, initIndex: Int = 0) {
    CareWheelPicker(
        items = list,
        onItemSelected = {},
        initIndex = initIndex,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

@Preview(name = "WheelPicker_Default_Hour", showBackground = true, group = "Default")
@Composable
private fun PreviewCareWheelPickerHourDefault() {
    CareWheelPickerContent(list = (1..12).toList())
}

@Preview(name = "WheelPicker_Default_Minute", showBackground = true, group = "Default")
@Composable
private fun PreviewCareWheelPickerMinuteDefault() {
    CareWheelPickerContent(list = (0..50 step 10).toList())
}

@Preview(name = "WheelPicker_Default_String", showBackground = true, group = "Default")
@Composable
private fun PreviewCareWheelPickerStringDefault() {
    CareWheelPickerContent(list = listOf("오전", "오후"), initIndex = 1)
}

@Preview(name = "WheelPicker_Flip_Hour", showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewCareWheelPickerHourFlip() {
    CareWheelPickerContent(list = (1..12).toList())
}

@Preview(name = "WheelPicker_Flip_Minute", showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewCareWheelPickerMinuteFlip() {
    CareWheelPickerContent(list = (0..50 step 10).toList())
}

@Preview(name = "WheelPicker_Flip_String", showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewCareWheelPickerStringFlip() {
    CareWheelPickerContent(list = listOf("오전", "오후"), initIndex = 1)
}

@Preview(
    name = "WheelPicker_Foldable_Hour",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareWheelPickerHourFoldable() {
    CareWheelPickerContent(list = (1..12).toList())
}

@Preview(
    name = "WheelPicker_Foldable_Minute",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareWheelPickerMinuteFoldable() {
    CareWheelPickerContent(list = (0..50 step 10).toList())
}

@Preview(
    name = "WheelPicker_Foldable_String",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareWheelPickerStringFoldable() {
    CareWheelPickerContent(list = listOf("오전", "오후"), initIndex = 1)
}