package com.idle.designsystem.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.idle.compose.clickable
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareSubtitleTopAppBar(
    title: String,
    onNavigationClick: () -> Unit = {},
    leftComponent: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(com.idle.designresource.R.drawable.ic_arrow_left),
            contentDescription = null,
            modifier = Modifier.clickable { onNavigationClick() }
        )

        Text(
            text = title,
            style = CareTheme.typography.subtitle1,
        )

        Spacer(modifier = Modifier.weight(1f))

        leftComponent()
    }
}

@Composable
fun CareHeadingTopAppBar(
    title: String,
    rightComponent: @Composable () -> Unit = {},
    leftComponent: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        rightComponent()

        Text(
            text = title,
            style = CareTheme.typography.heading2,
        )

        Spacer(modifier = Modifier.weight(1f))

        leftComponent()
    }
}