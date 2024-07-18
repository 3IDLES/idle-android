package com.idle.designsystem.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.designsystem.compose.foundation.Gray500
import com.idle.designsystem.compose.foundation.Gray900
import com.idle.designsystem.compose.foundation.Orange500
import com.idle.designsystem.compose.foundation.White000

@Composable
fun CareDialog(
    title: String,
    titleColor: Color = CareTheme.colors.gray900,
    description: String? = null,
    descriptionColor: Color = CareTheme.colors.gray500,
    leftButtonText: String,
    leftButtonTextColor: Color = CareTheme.colors.white000,
    leftButtonColor: Color = CareTheme.colors.white000,
    rightButtonText: String,
    rightButtonTextColor: Color = CareTheme.colors.orange500,
    rightButtonColor: Color = CareTheme.colors.white000,
    onDismissRequest: () -> Unit = {},
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            colors = CardColors(
                contentColor = CareTheme.colors.white000,
                containerColor = CareTheme.colors.white000,
                disabledContentColor = CareTheme.colors.white000,
                disabledContainerColor = CareTheme.colors.white000,
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = modifier,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(12.dp),
            ) {
                val titleModifier =
                    if (description != null) Modifier.padding(top = 12.dp, bottom = 16.dp)
                    else Modifier.padding(top = 8.dp)

                Text(
                    text = title,
                    style = CareTheme.typography.subtitle1,
                    color = titleColor,
                    modifier = titleModifier,
                )

                if (description != null) {
                    Text(
                        text = description,
                        style = CareTheme.typography.body3,
                        color = descriptionColor,
                        modifier = Modifier.padding(bottom = 16.dp),
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    CareDialogButton(
                        text = leftButtonText,
                        textColor = leftButtonTextColor,
                        containerColor = leftButtonColor,
                        onClick = onLeftButtonClick,
                        modifier = Modifier.weight(1f),
                    )

                    CareDialogButton(
                        text = rightButtonText,
                        textColor = rightButtonTextColor,
                        containerColor = rightButtonColor,
                        onClick = onRightButtonClick,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}