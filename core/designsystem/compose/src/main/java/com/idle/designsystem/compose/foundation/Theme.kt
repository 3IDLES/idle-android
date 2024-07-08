package com.idle.designsystem.compose.foundation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalColors = staticCompositionLocalOf { CareColors() }
val LocalTypography = staticCompositionLocalOf { CareTypography() }

// Color Scheme이 정의 되지 않았고, dark,light 별 색상이 정의 되지 않아서 아직은 사용 하지 않았습니다!
@Composable
fun CareTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider {
        MaterialTheme(
            content = content
        )
    }
}

object CareTheme {
    val colors: CareColors
        @Composable get() = LocalColors.current
    val typography: CareTypography
        @Composable get() = LocalTypography.current
}