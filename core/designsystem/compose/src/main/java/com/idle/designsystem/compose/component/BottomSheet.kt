package com.idle.designsystem.compose.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.foundation.CareTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CareBottomSheetLayout(
    sheetState: ModalBottomSheetState,
    sheetContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetGesturesEnabled = false,
        sheetContentColor = CareTheme.colors.white000,
        sheetBackgroundColor = CareTheme.colors.white000,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetState = sheetState,
        sheetContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = 18.dp, end = 18.dp, bottom = 32.dp)
            ) {
                HorizontalDivider(
                    thickness = 4.dp,
                    color = CareTheme.colors.gray200,
                    modifier = Modifier
                        .width(55.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .padding(top = 13.dp, bottom = 19.dp),
                )

                sheetContent()
            }
        },
        modifier = modifier.fillMaxWidth(),
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewDatePickerBottomSheet() {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Expanded,
        confirmValueChange = { false },
    )

    CareBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "접수 마감일",
                    style = CareTheme.typography.heading3,
                    color = CareTheme.colors.gray900,
                )

                CareCalendar(
                    year = 2024,
                    month = 8,
                    startMonth = 7,
                    selectedDate = LocalDate.of(2024, 8, 6),
                    onMonthChanged = {},
                    onDayClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize())
    }
}
