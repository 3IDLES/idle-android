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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.foundation.CareTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> CareWheelPicker(
    list: List<T>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    initIndex: Int = 0,
    pickerMaxHeight: Dp = 150.dp,
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initIndex)
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    // Int 타입이면 padStart 적용, 아니면 기본 toString 사용
    val innerList = listOf("") + list.map { item ->
        when (item) {
            is Int -> item.toString().padStart(2, '0')
            else -> item.toString()
        }
    } + listOf("")

    LaunchedEffect(listState.isScrollInProgress) {
        val currentIndex = listState.firstVisibleItemIndex + 1
        onItemSelected(innerList[currentIndex])
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
            itemsIndexed(items = innerList) { idx, item ->
                if (idx == listState.firstVisibleItemIndex + 1) {
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
                        modifier = Modifier.height(30.dp),
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

@Preview
@Composable
fun PreviewCareWheelPickerHour() {
    CareWheelPicker(
        list = (1..12).toList(),
        onItemSelected = { },
    )
}

@Preview
@Composable
fun PreviewCareWheelPickerMinute() {
    CareWheelPicker(
        list = (0..50 step 10).toList(),
        onItemSelected = {},
    )
}

@Preview
@Composable
fun PreviewCareWheelPickerString() {
    CareWheelPicker(
        list = listOf("오전", "오후"),
        initIndex = 1,
        onItemSelected = {},
    )
}

@Preview(
    name = "Foldable",
    showBackground = true,
    device = Devices.FOLDABLE
)
@Composable
fun PreviewCareWheelPickerFoldable() {
    CareWheelPicker(
        list = (1..24).toList(),
        onItemSelected = { },
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

@Preview(
    name = "Flip",
    showBackground = true,
    device = "spec:width=480dp,height=320dp,dpi=480,isRound=false,chinSize=0dp"
)
@Composable
fun PreviewCareWheelPickerFlip() {
    CareWheelPicker(
        list = listOf("오전", "오후"),
        onItemSelected = {},
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}
