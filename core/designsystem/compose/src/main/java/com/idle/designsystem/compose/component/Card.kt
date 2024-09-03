package com.idle.designsystem.compose.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idle.compose.clickable
import com.idle.designsystem.compose.FLIP
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareCard(
    title: String,
    modifier: Modifier = Modifier,
    titleTextColor: Color = CareTheme.colors.gray900,
    description: String? = null,
    titleLeftComponent: @Composable () -> Unit = {},
    descriptionLeftComponent: @Composable () -> Unit = {},
    showRightArrow: Boolean = true,
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
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    titleLeftComponent()

                    Text(
                        text = title,
                        style = CareTheme.typography.subtitle3,
                        color = titleTextColor,
                    )
                }

                if (description != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        descriptionLeftComponent()

                        Text(
                            text = description,
                            style = CareTheme.typography.body3,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = CareTheme.colors.gray500,
                        )
                    }
                }
            }

            if (showRightArrow) {
                Image(
                    painter = painterResource(com.idle.designresource.R.drawable.ic_arrow_right),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 30.dp),
                )
            }
        }
    }
}

@Composable
private fun CareCenterInfoCardPreviewContent(modifier: Modifier = Modifier) {
    CareCard(
        title = "John Doe",
        description = "1234 Elm Street",
        descriptionLeftComponent = {
            Image(
                painter = painterResource(com.idle.designresource.R.drawable.ic_address_pin),
                contentDescription = null,
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Preview(name = "CareCenterInfoCard_Default", showBackground = true, group = "Default")
@Composable
private fun PreviewCareCenterInfoCardDefault() {
    CareCenterInfoCardPreviewContent()
}

@Preview(name = "CareCenterInfoCard_Flip", showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewCareCenterInfoCardFlip() {
    CareCenterInfoCardPreviewContent()
}

@Preview(
    name = "CareCenterInfoCard_Fold",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareCenterInfoCardFoldable() {
    CareCenterInfoCardPreviewContent()
}

@Composable
private fun CareContactCardPreviewContent(modifier: Modifier = Modifier) {
    CareCard(
        title = "전화로 문의하기",
        description = "네 얼간이 요양보호센터 | 010-1234-5678",
        titleLeftComponent = {
            Image(
                painter = painterResource(com.idle.designresource.R.drawable.ic_call),
                contentDescription = null,
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Preview(name = "CareContactCard_Default", showBackground = true, group = "Default")
@Composable
private fun PreviewCareContactCardDefault() {
    CareContactCardPreviewContent()
}

@Preview(name = "CareContactCard_Flip", showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewCareContactCardFlip() {
    CareContactCardPreviewContent()
}

@Preview(
    name = "CareContactCard_Fold",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareContactCardFoldable() {
    CareContactCardPreviewContent()
}

@Composable
private fun CareCenterEditCardPreviewContent(modifier: Modifier = Modifier) {
    CareCard(
        title = "공고 수정하기",
        titleLeftComponent = {
            Image(
                painter = painterResource(id = com.idle.designresource.R.drawable.ic_edit_pencil_non_background),
                contentDescription = null,
                colorFilter = ColorFilter.tint(CareTheme.colors.gray500),
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}


@Preview(name = "CareCenterEditCard_Default", showBackground = true, group = "Default")
@Composable
private fun PreviewCareCenterCardDefault() {
    CareCenterEditCardPreviewContent()
}

@Preview(name = "CareCenterEditCard_Flip", showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewCareCenterCardFlip() {
    CareCenterEditCardPreviewContent()
}

@Preview(
    name = "CareCenterEditCard_Fold",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareCenterCardFoldable() {
    CareCenterEditCardPreviewContent()
}