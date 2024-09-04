package com.idle.designsystem.compose.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.idle.compose.clickable
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareToggleText(
    rightChecked: Boolean,
    leftText: String,
    rightText: String,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val screenWidthPx = with(density) { 84.dp.toPx().toInt() }
    val targetOffset = if (rightChecked) {
        IntOffset(screenWidthPx, 0)
    } else {
        IntOffset.Zero
    }

    val thumbXOffset by animateIntOffsetAsState(
        targetValue = targetOffset,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "Thumb Animation",
    )

    val rightTextColor by animateColorAsState(
        targetValue = if (rightChecked) CareTheme.colors.gray900
        else CareTheme.colors.gray300
    )

    val leftTextColor by animateColorAsState(
        targetValue = if (!rightChecked) CareTheme.colors.gray900
        else CareTheme.colors.gray300
    )

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .width(188.dp)
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(18.dp))
            .background(CareTheme.colors.gray050)
            .clickable { onCheckedChanged(!rightChecked) }
    ) {
        Box(
            modifier = Modifier
                .padding(2.dp)
                .width(100.dp)
                .fillMaxHeight()
                .graphicsLayer { translationX = thumbXOffset.x.toFloat() }
                .clip(RoundedCornerShape(16.dp))
                .background(CareTheme.colors.white000),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = leftText,
                style = CareTheme.typography.body3,
                color = leftTextColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(72.dp)
            )

            Text(
                text = rightText,
                style = CareTheme.typography.body3,
                color = rightTextColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(72.dp)
            )
        }
    }
}