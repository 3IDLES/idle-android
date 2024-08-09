package com.idle.designsystem.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idle.compose.clickable
import com.idle.designsystem.compose.FLIP
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareSubtitleTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onNavigationClick: () -> Unit = {},
    leftComponent: @Composable () -> Unit = {},
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
fun CareHeadingTopBar(
    title: String,
    modifier: Modifier = Modifier,
    rightComponent: @Composable () -> Unit = {},
    leftComponent: @Composable () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        rightComponent()

        Text(
            text = title,
            style = CareTheme.typography.heading1,
        )

        Spacer(modifier = Modifier.weight(1f))

        leftComponent()
    }
}

@Composable
private fun CareSubtitleTopBarContent() {
    CareSubtitleTopBar(
        title = "Subtitle Bar",
        onNavigationClick = {},
        leftComponent = {
            Text(text = "Left Action", style = CareTheme.typography.body3)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
private fun CareHeadingTopBarContent() {
    CareHeadingTopBar(
        title = "Heading Bar",
        rightComponent = {
            Text(text = "Right Action", style = CareTheme.typography.body3)
        },
        leftComponent = {
            Text(text = "Left Action", style = CareTheme.typography.body3)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}


@Preview(name = "SubtitleTopBar_Default", showBackground = true, group = "Default")
@Composable
private fun PreviewCareSubtitleTopBarDefault() {
    CareSubtitleTopBarContent()
}

@Preview(name = "SubtitleTopBar_Flip", showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewCareSubtitleTopBarFlip() {
    CareSubtitleTopBarContent()
}

@Preview(
    name = "SubtitleTopBar_Foldable",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareSubtitleTopBarFoldable() {
    CareSubtitleTopBarContent()
}

@Preview(name = "HeadingTopBar_Default", showBackground = true, group = "Default")
@Composable
private fun PreviewCareHeadingTopBarDefault() {
    CareHeadingTopBarContent()
}

@Preview(name = "HeadingTopAppBar_Flip", showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewCareHeadingTopAppBarFlip() {
    CareHeadingTopBarContent()
}

@Preview(
    name = "HeadingTopBar_Foldable",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareHeadingTopBarFoldable() {
    CareHeadingTopBarContent()
}