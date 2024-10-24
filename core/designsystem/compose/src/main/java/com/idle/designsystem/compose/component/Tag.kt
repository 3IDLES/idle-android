package com.idle.designsystem.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareTag(
    text: String,
    textColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
    ) {
        Text(
            text = text,
            style = CareTheme.typography.caption1,
            color = textColor,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
        )
    }
}

@Composable
private fun CareTagContent() {
    CareTag(
        text = "Sample Tag",
        textColor = CareTheme.colors.white000,
        backgroundColor = CareTheme.colors.orange500,
        modifier = Modifier.wrapContentSize()
    )
}

@Preview(name = "Tag_Default", showBackground = true, group = "Default")
@Composable
private fun PreviewCareTagDefault() {
    CareTagContent()
}