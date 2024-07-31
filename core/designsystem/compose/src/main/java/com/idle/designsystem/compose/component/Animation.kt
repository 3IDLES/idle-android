package com.idle.designsystem.compose.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <S : Enum<S>> CareStateAnimator(
    targetState: S,
    modifier: Modifier = Modifier,
    label : String = "AnimatedContent",
    content: @Composable() AnimatedContentScope.(targetState: S) -> Unit
) {
    AnimatedContent(
        targetState = targetState,
        transitionSpec = {
            if (targetState.ordinal > initialState.ordinal) {
                slideInHorizontally(initialOffsetX = { it }) + fadeIn() togetherWith
                        slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
            } else {
                slideInHorizontally(initialOffsetX = { -it }) + fadeIn() togetherWith
                        slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
            }
        },
        label = label,
        content = content,
        modifier = modifier,
    )
}