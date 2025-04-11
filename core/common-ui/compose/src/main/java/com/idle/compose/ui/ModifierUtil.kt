package com.idle.compose.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.addFocusCleaner(
    focusManager: FocusManager,
    doOnClear: () -> Unit = {},
): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(
            onTap = {
                doOnClear()
                focusManager.clearFocus()
            },
        )
    }
}

@Composable
fun Modifier.clickable(
    enabled: Boolean = true,
    throttleTime: Long = 0L,
    onClick: () -> Unit,
): Modifier = composed {
    var lastClickTime by remember { mutableStateOf(0L) }

    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled = enabled,
    ) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= throttleTime) {
            onClick()
            lastClickTime = currentTime
        }
    }
}
