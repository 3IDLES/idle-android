package com.idle.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun LabeledContent(
    subtitle: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier,
    ) {
        Text(
            text = subtitle,
            style = CareTheme.typography.subtitle4,
            color = CareTheme.colors.gray500
        )

        content()
    }
}