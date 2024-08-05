package com.idle.designsystem.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idle.compose.clickable
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareChipBasic(
    text: String,
    enable: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(44.dp)
            .background(
                color = if (enable) CareTheme.colors.orange100 else CareTheme.colors.white000,
                shape = RoundedCornerShape(6.dp),
            )
            .border(
                width = 1.dp,
                color = if (enable) CareTheme.colors.orange400 else CareTheme.colors.gray100,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onClick() },
    ) {
        Text(
            text = text,
            style = CareTheme.typography.body3,
            color = if (enable) CareTheme.colors.orange400 else CareTheme.colors.gray500,
        )
    }
}

@Composable
fun CareChipShort(
    text: String,
    enable: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(40.dp)
            .background(
                color = if (enable) CareTheme.colors.orange100 else CareTheme.colors.white000,
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                width = 1.dp,
                color = if (enable) CareTheme.colors.orange400 else CareTheme.colors.gray100,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onClick() },
    ) {
        Text(
            text = text,
            style = CareTheme.typography.body2,
            color = if (enable) CareTheme.colors.orange400 else CareTheme.colors.gray400,
        )
    }
}

@Preview(name = "CareChipBasic_Default", showBackground = true, group = "Default")
@Composable
private fun PreviewCareChipBasicDefault() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        CareChipBasic(
            text = "Basic",
            enable = true,
            modifier = Modifier.width(104.dp),
        )
        CareChipBasic(
            text = "Basic",
            enable = false,
            modifier = Modifier.width(104.dp),
        )
    }
}

@Preview(name = "CareChipShort_Default", showBackground = true, group = "Default")
@Composable
private fun PreviewCareChipShortDefault() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        CareChipShort(
            text = "S",
            enable = true,
            modifier = Modifier
        )
        CareChipShort(
            text = "S",
            enable = false,
            modifier = Modifier
        )
    }
}