package com.idle.designsystem.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun ConditionRow(
    isValid: Boolean,
    conditionText: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        val imageResource = if (isValid) com.idle.designresource.R.drawable.ic_right_condition
        else com.idle.designresource.R.drawable.ic_not_right_condition

        Image(
            painter = painterResource(imageResource),
            contentDescription = null,
        )

        val textColor = if (isValid) CareTheme.colors.green else CareTheme.colors.red

        Text(
            text = conditionText,
            style = CareTheme.typography.body3,
            color = textColor,
        )
    }
}