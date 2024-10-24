package com.idle.designsystem.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun LabeledContent(
    subtitle: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier,
    ) {
        Text(
            text = subtitle,
            style = CareTheme.typography.subtitle4,
            color = CareTheme.colors.gray500,
            modifier = Modifier.padding(bottom = 6.dp),
        )

        content()
    }
}

@Composable
fun LabeledContent(
    subtitle: AnnotatedString,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier,
    ) {
        Text(
            text = subtitle,
            style = CareTheme.typography.subtitle4,
            color = CareTheme.colors.gray500,
            modifier = Modifier.padding(bottom = 6.dp),
        )

        content()
    }
}