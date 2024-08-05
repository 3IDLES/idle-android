package com.idle.designsystem.compose.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults.ProgressAnimationSpec
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idle.designsystem.compose.Flip
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareProgressBar(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
) {
    val progress by animateFloatAsState(
        targetValue = (currentStep / totalSteps.toFloat()),
        animationSpec = ProgressAnimationSpec,
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        LinearProgressIndicator(
            progress = { progress },
            color = CareTheme.colors.orange500,
            trackColor = CareTheme.colors.gray100,
            strokeCap = StrokeCap.Round,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .height(4.dp)
                .weight(1f),
        )

        Text(
            text = formatProgressText(currentStep, totalSteps),
            style = CareTheme.typography.subtitle3,
        )
    }
}

@Composable
private fun formatProgressText(
    currentStep: Int,
    totalSteps: Int,
) = buildAnnotatedString {
    withStyle(
        style = SpanStyle(color = CareTheme.colors.orange500)
    ) {
        append("$currentStep ")
    }
    withStyle(
        style = SpanStyle(
            color = CareTheme.colors.gray300,
            fontSize = 14.sp,
        )
    ) {
        append("/ $totalSteps")
    }
}

@Composable
private fun CareProgressBarContent() {
    CareProgressBar(
        currentStep = 3,
        totalSteps = 5,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(name = "Progress_Default", showBackground = true, group = "Default")
@Composable
private fun PreviewCareProgressBarDefault() {
    CareProgressBarContent()
}

@Preview(name = "Progress_Flip", showBackground = true, device = Flip, group = "Flip")
@Composable
private fun PreviewCareProgressBarFlip() {
    CareProgressBarContent()
}

@Preview(
    name = "Progress_Foldable",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareProgressBarFoldable() {
    CareProgressBarContent()
}