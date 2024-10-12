package com.idle.designsystem.compose.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.idle.compose.clickable
import com.idle.designsystem.compose.FLIP
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
inline fun <reified S : Enum<S>> CareTabBar(
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
                        color = if (selectedStatus == status) CareTheme.colors.black
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
            color = CareTheme.colors.black,
            modifier = Modifier
                .width(screenWidthDp / 2)
                .align(Alignment.BottomStart)
                .padding(bottom = 4.dp)
                .graphicsLayer { translationX = topBarXOffset.x.toFloat() },
        )
    }
}

@Composable
private fun CareTabBarContent() {
    var selectedStatus by remember { mutableStateOf(HomeStatus.ACTIVE) }

    CareTabBar(
        selectedStatus = selectedStatus,
        setStatus = { status -> selectedStatus = status },
        displayName = { it.displayName }
    )
}


@Preview(name = "TabBar_Default", showBackground = true, group = "Default")
@Composable
private fun PreviewCareTabBarDefault() {
    CareTabBarContent()
}

@Preview(name = "TabBar_Flip", showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewCareTapBarFlip() {
    CareTabBarContent()
}

@Preview(
    name = "TapBar_Foldable",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareTapBarFoldable() {
    CareTabBarContent()
}

private enum class HomeStatus(val displayName: String) {
    ACTIVE("Active"),
    INACTIVE("Inactive")
}