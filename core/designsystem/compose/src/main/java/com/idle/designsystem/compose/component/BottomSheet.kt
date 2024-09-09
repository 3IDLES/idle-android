package com.idle.designsystem.compose.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.FLIP
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
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 36.dp,
                    bottom = 28.dp
                ),
            ) {
                sheetContent()
            }
        },
        modifier = modifier.fillMaxWidth(),
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PreviewCalendarBottomSheet() {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Expanded,
        confirmValueChange = { false }
    )

    CareBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = { CalendarBottomSheetContent() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun CalendarBottomSheetContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "접수 마감일",
            style = CareTheme.typography.heading3,
            color = CareTheme.colors.gray900
        )

        CareCalendar(
            year = 2024,
            month = 8,
            startDate = LocalDate.of(2024,7,1),
            selectedDate = LocalDate.of(2024, 8, 6),
            onMonthChanged = {},
            onDayClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PreviewWheelPickerBottomSheet() {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Expanded,
        confirmValueChange = { false }
    )

    CareBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = { WheelPickerBottomSheetContent() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun WheelPickerBottomSheetContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "근무 시작 시간",
            style = CareTheme.typography.heading3,
            color = CareTheme.colors.gray900
        )

        Spacer(modifier = Modifier.height(80.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            CareWheelPicker(
                items = listOf("오전", "오후"),
                onItemSelected = {},
                modifier = Modifier.padding(end = 40.dp)
            )

            CareWheelPicker(
                items = (1..12).toList(),
                onItemSelected = {},
                modifier = Modifier.padding(end = 10.dp)
            )

            Text(
                text = ":",
                style = CareTheme.typography.subtitle2,
                color = CareTheme.colors.gray900,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(20.dp)
            )

            CareWheelPicker(
                items = (0..50 step 10).toList(),
                onItemSelected = {},
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(80.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            CareButtonMedium(
                text = "취소",
                onClick = {},
                border = BorderStroke(width = 1.dp, color = CareTheme.colors.orange400),
                containerColor = CareTheme.colors.white000,
                textColor = CareTheme.colors.orange500,
                modifier = Modifier.weight(1f)
            )

            CareButtonMedium(
                text = "저장",
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, group = "Default")
@Composable
private fun PreviewCalendarBottomSheetDefault() {
    PreviewCalendarBottomSheet()
}

@Preview(showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewCalendarBottomSheetFlip() {
    PreviewCalendarBottomSheet()
}

@Preview(showBackground = true, device = Devices.FOLDABLE, group = "Fold")
@Composable
private fun PreviewCalendarBottomSheetFoldable() {
    PreviewCalendarBottomSheet()
}

@Preview(showBackground = true, group = "Default")
@Composable
private fun PreviewWheelPickerBottomSheetDefault() {
    PreviewWheelPickerBottomSheet()
}

@Preview(showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewWheelPickerBottomSheetFlip() {
    PreviewWheelPickerBottomSheet()
}

@Preview(showBackground = true, device = Devices.FOLDABLE, group = "Fold")
@Composable
private fun PreviewWheelPickerBottomSheetFoldable() {
    PreviewWheelPickerBottomSheet()
}
