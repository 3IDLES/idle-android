package com.idle.designsystem.compose.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.idle.compose.clickable
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareCard(
    name: String,
    address: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray100),
        colors = CardColors(
            containerColor = CareTheme.colors.white000,
            contentColor = CareTheme.colors.white000,
            disabledContentColor = CareTheme.colors.white000,
            disabledContainerColor = CareTheme.colors.white000,
        ),
        modifier = modifier.clickable(onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = name,
                    style = CareTheme.typography.subtitle3,
                    color = CareTheme.colors.gray900,
                )

                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Image(
                        painter = painterResource(com.idle.designresource.R.drawable.ic_address_pin),
                        contentDescription = null,
                    )

                    Text(
                        text = address,
                        style = CareTheme.typography.body3,
                        color = CareTheme.colors.gray500,
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(com.idle.designresource.R.drawable.ic_arrow_right),
                contentDescription = null,
            )
        }
    }
}