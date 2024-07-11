package com.idle.designsystem.compose.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareButtonSmall(
    text: String,
    onClick: () -> Unit,
    enable: Boolean,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enable,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonColors(
            containerColor = CareTheme.colors.orange500,
            contentColor = CareTheme.colors.orange500,
            disabledContentColor = CareTheme.colors.gray200,
            disabledContainerColor = CareTheme.colors.gray200,
        ),
        modifier = modifier.sizeIn(minWidth = 72.dp, minHeight = 44.dp),
    ) {
        Text(
            text = text,
            style = CareTheme.typography.heading4,
            color = CareTheme.colors.white000,
        )
    }
}

@Composable
fun CareButtonMedium(
    text: String,
    onClick: () -> Unit,
    enable: Boolean,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enable,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonColors(
            containerColor = CareTheme.colors.orange500,
            contentColor = CareTheme.colors.orange500,
            disabledContentColor = CareTheme.colors.gray200,
            disabledContainerColor = CareTheme.colors.gray200,
        ),
        modifier = modifier.sizeIn(minWidth = 165.dp, minHeight = 52.dp),
    ) {
        Text(
            text = text,
            style = CareTheme.typography.heading4,
            color = CareTheme.colors.white000,
        )
    }
}

@Composable
fun CareButtonLarge(
    text: String,
    onClick: () -> Unit,
    enable: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enable,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonColors(
            containerColor = CareTheme.colors.orange500,
            contentColor = CareTheme.colors.orange500,
            disabledContentColor = CareTheme.colors.gray200,
            disabledContainerColor = CareTheme.colors.gray200,
        ),
        modifier = modifier.sizeIn(minWidth = 328.dp, minHeight = 58.dp),
    ) {
        Text(
            text = text,
            style = CareTheme.typography.heading4,
            color = CareTheme.colors.white000,
        )
    }
}
