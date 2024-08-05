package com.idle.designsystem.compose.component

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.idle.designsystem.compose.Flip
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareDialog(
    title: String,
    leftButtonText: String,
    rightButtonText: String,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    titleColor: Color = CareTheme.colors.gray900,
    description: String? = null,
    descriptionColor: Color = CareTheme.colors.gray500,
    leftButtonTextColor: Color = CareTheme.colors.white000,
    leftButtonColor: Color = CareTheme.colors.white000,
    leftButtonBorder: BorderStroke? = null,
    rightButtonTextColor: Color = CareTheme.colors.orange500,
    rightButtonColor: Color = CareTheme.colors.white000,
    rightButtonBorder: BorderStroke? = null,
    onDismissRequest: () -> Unit = {},
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
                        textAlign = TextAlign.Center,
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
                        border = leftButtonBorder,
                        onClick = onLeftButtonClick,
                        modifier = Modifier.weight(1f),
                    )

                    CareDialogButton(
                        text = rightButtonText,
                        textColor = rightButtonTextColor,
                        containerColor = rightButtonColor,
                        border = rightButtonBorder,
                        onClick = onRightButtonClick,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun PreviewCareDialogBigContent() {
    CareDialog(
        title = "정말 탈퇴하시겠어요?",
        description = "탈퇴 버튼 선택 시 모든 정보가 삭제되며, 되돌릴 수 없습니다.",
        leftButtonText = "취소하기",
        rightButtonText = "탈퇴하기",
        onLeftButtonClick = {},
        onRightButtonClick = {},
        modifier = Modifier.fillMaxWidth(),
        leftButtonTextColor = CareTheme.colors.gray300,
        leftButtonColor = CareTheme.colors.white000,
        leftButtonBorder = BorderStroke(1.dp, CareTheme.colors.gray100),
        rightButtonTextColor = CareTheme.colors.white000,
        rightButtonColor = CareTheme.colors.orange500,
    )

}

@Composable
private fun PreviewCareDialogBigContent2() {
    CareDialog(
        title = "정말 탈퇴하시겠어요?",
        description = "탈퇴 버튼 선택 시 모든 정보가 삭제되며, 되돌릴 수 없습니다.",
        leftButtonText = "취소하기",
        rightButtonText = "탈퇴하기",
        onLeftButtonClick = {},
        onRightButtonClick = {},
        modifier = Modifier.fillMaxWidth(),
        leftButtonTextColor = CareTheme.colors.gray300,
        leftButtonColor = CareTheme.colors.white000,
        leftButtonBorder = BorderStroke(1.dp, CareTheme.colors.gray100),
        rightButtonTextColor = CareTheme.colors.white000,
        rightButtonColor = CareTheme.colors.red,
    )
}

@Composable
private fun PreviewCareDialogSmallContent() {
    CareDialog(
        title = "로그아웃하시겠어요?",
        leftButtonText = "취소하기",
        rightButtonText = "로그아웃",
        onLeftButtonClick = {},
        onRightButtonClick = {},
        modifier = Modifier.fillMaxWidth(),
        leftButtonTextColor = CareTheme.colors.gray300,
        leftButtonColor = CareTheme.colors.white000,
        leftButtonBorder = BorderStroke(1.dp, CareTheme.colors.gray100),
        rightButtonTextColor = CareTheme.colors.white000,
        rightButtonColor = CareTheme.colors.orange500,
    )
}

@Composable
private fun PreviewCareDialogSmallContent2() {
    CareDialog(
        title = "'홍길동'님을 채용할까요?",
        leftButtonText = "취소하기",
        rightButtonText = "채용하기",
        onLeftButtonClick = {},
        onRightButtonClick = {},
        modifier = Modifier.fillMaxWidth(),
        leftButtonTextColor = CareTheme.colors.gray300,
        leftButtonColor = CareTheme.colors.white000,
        leftButtonBorder = BorderStroke(1.dp, CareTheme.colors.gray100),
        rightButtonTextColor = CareTheme.colors.white000,
        rightButtonColor = CareTheme.colors.red,
    )
}

@Preview(name = "Modal_big_Default", showBackground = true, group = "Default")
@Composable
private fun PreviewCareDialogBigDefault() {
    PreviewCareDialogBigContent()
}

@Preview(name = "Modal_big_Default2", showBackground = true, group = "Default")
@Composable
private fun PreviewCareDialogBigDefault2() {
    PreviewCareDialogBigContent2()
}

@Preview(name = "Modal_small_Default", showBackground = true, group = "Default")
@Composable
private fun PreviewCareDialogSmallDefault() {
    PreviewCareDialogSmallContent()
}

@Preview(name = "Modal_small_Default2", showBackground = true, group = "Default")
@Composable
private fun PreviewCareDialogSmallDefault2() {
    PreviewCareDialogSmallContent2()
}

@Preview(name = "Modal_big_Flip", showBackground = true, device = Flip, group = "Flip")
@Composable
private fun PreviewCareDialogBigFlip() {
    PreviewCareDialogBigContent()
}

@Preview(name = "Modal_big_Flip2", showBackground = true, device = Flip, group = "Flip")
@Composable
private fun PreviewCareDialogBigFlip2() {
    PreviewCareDialogBigContent2()
}

@Preview(name = "Modal_small_Flip", showBackground = true, device = Flip, group = "Flip")
@Composable
private fun PreviewCareDialogSmallFlip() {
    PreviewCareDialogSmallContent()
}

@Preview(name = "Modal_small_Flip2", showBackground = true, device = Flip, group = "Flip")
@Composable
private fun PreviewCareDialogSmallFlip2() {
    PreviewCareDialogSmallContent2()
}

@Preview(name = "Modal_big_Fold", showBackground = true, device = Devices.FOLDABLE, group = "Fold")
@Composable
private fun PreviewCareDialogBigFold() {
    PreviewCareDialogBigContent()
}

@Preview(name = "Modal_big_Fold2", showBackground = true, device = Devices.FOLDABLE, group = "Fold")
@Composable
private fun PreviewCareDialogBigFold2() {
    PreviewCareDialogBigContent2()
}

@Preview(
    name = "Modal_small_Fold",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareDialogSmallFold() {
    PreviewCareDialogSmallContent()
}

@Preview(
    name = "Modal_small_Fold2",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareDialogSmallFold2() {
    PreviewCareDialogSmallContent2()
}