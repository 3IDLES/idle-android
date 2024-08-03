package com.idle.designsystem.compose.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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
        modifier = modifier.size(width = 72.dp, height = 44.dp),
    ) {
        Text(
            text = text,
            style = CareTheme.typography.subtitle4,
            color = CareTheme.colors.white000,
        )
    }
}

@Composable
fun CareButtonMedium(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
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
        modifier = modifier.size(width = 165.dp, height = 56.dp),
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
    modifier: Modifier = Modifier,
    enable: Boolean = true,
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
        modifier = modifier.height(58.dp),
    ) {
        Text(
            text = text,
            style = CareTheme.typography.heading4,
            color = CareTheme.colors.white000,
        )
    }
}

@Composable
fun CareButtonCardLarge(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enable,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonColors(
            containerColor = CareTheme.colors.orange500,
            contentColor = CareTheme.colors.orange500,
            disabledContentColor = CareTheme.colors.gray200,
            disabledContainerColor = CareTheme.colors.gray200,
        ),
        modifier = modifier.height(38.dp),
    ) {
        Text(
            text = text,
            style = CareTheme.typography.subtitle4,
            color = CareTheme.colors.white000,
        )
    }
}

@Composable
fun CareButtonCardMedium(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enable,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonColors(
            containerColor = CareTheme.colors.orange500,
            contentColor = CareTheme.colors.orange500,
            disabledContentColor = CareTheme.colors.gray200,
            disabledContainerColor = CareTheme.colors.gray200,
        ),
        modifier = modifier.height(38.dp),
    ) {
        Text(
            text = text,
            style = CareTheme.typography.heading4,
            color = CareTheme.colors.white000,
        )
    }
}

@Composable
fun CareButtonStrokeSmall(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enable,
        shape = RoundedCornerShape(19.dp),
        border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray100),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
        colors = ButtonColors(
            containerColor = CareTheme.colors.white000,
            contentColor = CareTheme.colors.white000,
            disabledContentColor = CareTheme.colors.white000,
            disabledContainerColor = CareTheme.colors.white000,
        ),
        modifier = modifier.height(32.dp),
    ) {
        Text(
            text = text,
            style = CareTheme.typography.body3,
            color = CareTheme.colors.gray300,
        )
    }
}

@Composable
fun CareButtonLine(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    borderColor: Color = CareTheme.colors.orange400,
    containerColor: Color = CareTheme.colors.white000,
    textColor: Color = CareTheme.colors.orange500,
) {
    Button(
        onClick = onClick,
        enabled = enable,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(width = 1.dp, color = borderColor),
        colors = ButtonColors(
            containerColor = containerColor,
            contentColor = CareTheme.colors.white000,
            disabledContentColor = CareTheme.colors.gray300,
            disabledContainerColor = CareTheme.colors.gray200,
        ),
        modifier = modifier.height(56.dp),
    ) {
        Text(
            text = text,
            style = CareTheme.typography.heading4,
            color = textColor,
        )
    }
}

@Composable
internal fun CareDialogButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    enable: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enable,
        shape = RoundedCornerShape(6.dp),
        border = border,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        colors = ButtonColors(
            containerColor = containerColor,
            contentColor = containerColor,
            disabledContentColor = containerColor,
            disabledContainerColor = containerColor,
        ),
        modifier = modifier,
    ) {
        Text(
            text = text,
            style = CareTheme.typography.heading4,
            color = textColor,
        )
    }
}