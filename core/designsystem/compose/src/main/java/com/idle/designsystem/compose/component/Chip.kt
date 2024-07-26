package com.idle.designsystem.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareChip(
    text: String,
    enable: Boolean,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
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
            color = if (enable) CareTheme.colors.orange400 else CareTheme.colors.gray400,
        )
    }
}