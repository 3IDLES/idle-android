package com.idle.designsystem.compose.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
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
            style = CareTheme.typography.heading2,
        )

        Spacer(modifier = Modifier.weight(1f))

        leftComponent()
    }
}

@Composable
inline fun <reified S : Enum<S>> CareHomeTopBar(
    selectedStatus: S,
    crossinline setStatus: (S) -> Unit,
    displayName: (S) -> String,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenWidthPx = with(density) { screenWidthDp.toPx().toInt() }

    val targetOffset = if (selectedStatus.ordinal == 1) {
        IntOffset(screenWidthPx / 2, 0)
    } else {
        IntOffset.Zero
    }

    val topBarXOffset by animateIntOffsetAsState(
        targetValue = targetOffset,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "TopBar Animation",
    )

    Box(modifier = modifier.wrapContentHeight()) {
        Row(modifier = Modifier.wrapContentHeight()) {
            enumValues<S>().forEach { status ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .clickable { setStatus(status) },
                ) {
                    Text(
                        text = displayName(status),
                        style = CareTheme.typography.subtitle3,
                        color = if (selectedStatus == status) CareTheme.colors.gray900
                        else CareTheme.colors.gray300,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 13.dp),
                    )

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = CareTheme.colors.gray100,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 4.dp),
                    )
                }
            }
        }

        HorizontalDivider(
            thickness = 2.dp,
            color = CareTheme.colors.gray900,
            modifier = Modifier
                .width(screenWidthDp / 2)
                .align(Alignment.BottomStart)
                .padding(bottom = 4.dp)
                .graphicsLayer { translationX = topBarXOffset.x.toFloat() },
        )
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

@Composable
private fun CareHomeTopBarContent() {
    var selectedStatus by remember { mutableStateOf(HomeStatus.ACTIVE) }

    CareHomeTopBar(
        selectedStatus = selectedStatus,
        setStatus = { status -> selectedStatus = status },
        displayName = { it.displayName }
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


@Preview(name = "HomeTopBar_Default", showBackground = true, group = "Default")
@Composable
private fun PreviewCareHomeTopBarDefault() {
    CareHomeTopBarContent()
}

@Preview(name = "HomeTopBar_Flip", showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewCareHomeTopBarFlip() {
    CareHomeTopBarContent()
}

@Preview(
    name = "HomeTopBar_Foldable",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareHomeTopBarFoldable() {
    CareHomeTopBarContent()
}

private enum class HomeStatus(val displayName: String) {
    ACTIVE("Active"),
    INACTIVE("Inactive")
}