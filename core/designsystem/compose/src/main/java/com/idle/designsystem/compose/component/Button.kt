package com.idle.designsystem.compose.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareButton(
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
        contentPadding = PaddingValues(vertical = 17.dp, horizontal = (67.5).dp),
        modifier = modifier,
    ) {
        Text(
            text = text,
            style = CareTheme.typography.heading4,
            color = CareTheme.colors.white000,
        )
    }
}