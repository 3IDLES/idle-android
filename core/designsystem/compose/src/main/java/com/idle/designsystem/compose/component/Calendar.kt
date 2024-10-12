package com.idle.designsystem.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idle.compose.clickable
import com.idle.designsystem.compose.FLIP
import com.idle.designsystem.compose.foundation.CareTheme
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CareCalendar(
    year: Int,
    month: Int,
    startDate: LocalDate,
    onMonthChanged: (Int) -> Unit,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate? = null,
) {
    var currentMonth by rememberSaveable { mutableIntStateOf(month) }
    var currentYear by rememberSaveable { mutableIntStateOf(year) }
    val startMonth = startDate.monthValue

    Column(
        verticalArrangement = Arrangement.spacedBy(26.dp),
        modifier = modifier,
    ) {
        CalendarHeader(
            year = currentYear,
            month = currentMonth,
            startMonth = startMonth,
            onMonthChanged = { newMonth ->
                if (newMonth < 1) {
                    currentMonth = 12
                    currentYear--
                } else if (newMonth > 12) {
                    currentMonth = 1
                    currentYear++
                } else {
                    currentMonth = newMonth
                }
            },
        )

        CareStateAnimator(
            targetState = currentMonth,
            transitionCondition = currentMonth != startMonth,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(26.dp),
            ) {
                DayOfWeekLabel()

                CalendarBody(
                    year = currentYear,
                    month = currentMonth,
                    selectedDate = selectedDate,
                    startDate = startDate,
                    onDayClick = {
                        onDayClick(it)
                        onMonthChanged(currentMonth)
                    },
                )
            }
        }
    }
}

@Composable
private fun CalendarHeader(
    year: Int,
    month: Int,
    startMonth: Int,
    onMonthChanged: (Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),
    ) {
        if (month == startMonth + 1) {
            Image(
                painter = painterResource(id = com.idle.designresource.R.drawable.ic_arrow_left_small),
                contentDescription = null,
                modifier = Modifier.clickable { onMonthChanged(month - 1) }
            )
        } else {
            Box(modifier = Modifier.size(24.dp))
        }

        Text(
            text = "${year}년 ${month}월",
            style = CareTheme.typography.subtitle2,
            color = CareTheme.colors.black,
        )

        if (month == startMonth) {
            Image(
                painter = painterResource(id = com.idle.designresource.R.drawable.ic_arrow_right_small),
                contentDescription = null,
                modifier = Modifier.clickable { onMonthChanged(month + 1) }
            )
        } else {
            Box(modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
private fun DayOfWeekLabel() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally,
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        DayOfWeek.entries.forEach { dayOfWeek ->
            Text(
                text = dayOfWeek.displayName,
                textAlign = TextAlign.Center,
                style = CareTheme.typography.body2,
                color = CareTheme.colors.gray300,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CalendarBody(
    year: Int,
    month: Int,
    selectedDate: LocalDate?,
    startDate: LocalDate,
    onDayClick: (LocalDate) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally,
        ),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        val calendarDate = LocalDate.of(year, month, 1)
        val visibleDaysCount = countVisibleDaysFromLastMonth(calendarDate)
        val beforeMonthDaysToShow = generateBeforeMonthDaysToShow(
            visibleDaysCount = visibleDaysCount,
            currentDate = calendarDate,
        )
        items(beforeMonthDaysToShow) { day ->
            CalendarDayText(
                day = day,
                color = CareTheme.colors.gray300,
                isSelected = (selectedDate?.monthValue == calendarDate.monthValue.minus(1)) &&
                        (selectedDate.dayOfMonth == day)
            )
        }

        val thisMonthLastDate = calendarDate.lengthOfMonth()
        val thisMonthDaysToShow: List<Int> = (1..thisMonthLastDate).toList()
        items(thisMonthDaysToShow) { day ->
            val currentDate = calendarDate.withDayOfMonth(day)
            val isBeforeStartDate = currentDate.isBefore(startDate)

            CalendarDayText(
                day = day,
                color = if (isBeforeStartDate) CareTheme.colors.gray300 else CareTheme.colors.gray700,
                isSelected = (selectedDate?.monthValue == calendarDate.monthValue) &&
                        (selectedDate.dayOfMonth == day),
                onClick = {
                    if (!isBeforeStartDate) {
                        onDayClick(currentDate)
                    }
                }
            )
        }

        val remainingDays = 42 - (visibleDaysCount + thisMonthDaysToShow.size)
        val nextMonthDaysToShow = IntRange(1, remainingDays).toList()
        items(nextMonthDaysToShow) { day ->
            CalendarDayText(
                day = day,
                color = CareTheme.colors.gray300,
                isSelected = (selectedDate?.monthValue == calendarDate.monthValue.plus(1)) &&
                        (selectedDate.dayOfMonth == day)
            )
        }
    }
}

@Composable
private fun CalendarDayText(
    day: Int,
    color: Color,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(32.dp)
            .clickable { onClick() }
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(CareTheme.colors.orange500)
            )
        }

        Text(
            text = day.toString(),
            style = CareTheme.typography.body2,
            color = if (isSelected) CareTheme.colors.white000 else color,
        )
    }
}

private fun countVisibleDaysFromLastMonth(currentDate: LocalDate): Int {
    val firstDayOfWeek = currentDate.withDayOfMonth(1).dayOfWeek
    val firstDayDisplayName = firstDayOfWeek.getDisplayName(TextStyle.FULL, Locale.US).uppercase()
    var count = 0
    for (day in DayOfWeek.entries) {
        if (day.name == firstDayDisplayName) break
        count += 1
    }
    return count
}

private fun generateBeforeMonthDaysToShow(
    visibleDaysCount: Int,
    currentDate: LocalDate,
): List<Int> {
    val beforeMonth = currentDate.minusMonths(1)
    val beforeMonthLastDay = beforeMonth.lengthOfMonth()
    return IntRange(beforeMonthLastDay - visibleDaysCount + 1, beforeMonthLastDay).toList()
}

enum class DayOfWeek(val displayName: String) {
    MONDAY("월"),
    TUESDAY("화"),
    WEDNESDAY("수"),
    THURSDAY("목"),
    FRIDAY("금"),
    SATURDAY("토"),
    SUNDAY("일"),
}

@Preview(
    name = "Calendar_Default_WithOutSelect",
    showBackground = true,
    group = "Default"
)
@Composable
private fun PreviewCareCalendarDefaultWithoutSelect() {
    CareCalendar(
        year = 2024,
        month = 7,
        startDate = LocalDate.now(),
        onMonthChanged = {},
        onDayClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    )
}

@Preview(
    name = "Calendar_Default_WithSelect",
    showBackground = true,
    group = "Default"
)
@Composable
private fun PreviewCareCalendarDefaultWithSelect() {
    CareCalendar(
        year = 2024,
        month = 8,
        startDate = LocalDate.now(),
        selectedDate = LocalDate.of(2024, 8, 6),
        onMonthChanged = {},
        onDayClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    )
}

@Preview(
    name = "Calendar_Foldable_WithOutSelect",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareCalendarFoldableWithoutSelect() {
    CareCalendar(
        year = 2024,
        month = 7,
        startDate = LocalDate.now(),
        onMonthChanged = {},
        onDayClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    )
}

@Preview(
    name = "Calendar_Foldable_WithSelect",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareCalendarFoldableWithSelect() {
    CareCalendar(
        year = 2024,
        month = 7,
        startDate = LocalDate.now(),
        selectedDate = LocalDate.of(2024, 7, 4),
        onMonthChanged = {},
        onDayClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    )
}

@Preview(
    name = "Calendar_Flip_WithOutSelect",
    showBackground = true,
    device = FLIP,
    group = "Flip"
)
@Composable
private fun PreviewCareCalendarFlipWithoutSelect() {
    CareCalendar(
        year = 2024,
        month = 8,
        startDate = LocalDate.now(),
        onMonthChanged = {},
        onDayClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    )
}

@Preview(
    name = "Calendar_Flip_WithSelect",
    showBackground = true,
    device = FLIP,
    group = "Flip"
)
@Composable
private fun PreviewCareCalendarFlipWithSelect() {
    CareCalendar(
        year = 2024,
        month = 8,
        startDate = LocalDate.now(),
        selectedDate = LocalDate.of(2024, 8, 6),
        onMonthChanged = {},
        onDayClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    )
}
