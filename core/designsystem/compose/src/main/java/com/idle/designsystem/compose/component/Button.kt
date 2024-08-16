package com.idle.designsystem.compose.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.FLIP
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
    containerColor: Color = CareTheme.colors.orange500,
    textColor: Color = CareTheme.colors.white000,
    border: BorderStroke? = null,
) {
    Button(
        onClick = onClick,
        enabled = enable,
        shape = RoundedCornerShape(8.dp),
        border = border,
        colors = ButtonColors(
            containerColor = containerColor,
            contentColor = containerColor,
            disabledContentColor = CareTheme.colors.gray200,
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
    containerColor: Color = CareTheme.colors.orange500,
    textColor: Color = CareTheme.colors.white000,
    border: BorderStroke? = null,
) {
    Button(
        onClick = onClick,
        enabled = enable,
        border = border,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonColors(
            containerColor = containerColor,
            contentColor = containerColor,
            disabledContentColor = CareTheme.colors.gray200,
            disabledContainerColor = CareTheme.colors.gray200,
        ),
        modifier = modifier.height(38.dp),
    ) {
        Text(
            text = text,
            style = CareTheme.typography.subtitle4,
            color = textColor,
        )
    }
}

@Composable
fun CareButtonRound(
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
        border = BorderStroke(
            width = 1.dp,
            color = if (!enable) CareTheme.colors.gray300 else borderColor
        ),
        colors = ButtonColors(
            containerColor = containerColor,
            contentColor = CareTheme.colors.white000,
            disabledContentColor = CareTheme.colors.gray050,
            disabledContainerColor = CareTheme.colors.gray050,
        ),
        modifier = modifier.height(56.dp),
    ) {
        Text(
            text = text,
            style = CareTheme.typography.heading4,
            color = if (!enable) CareTheme.colors.gray300 else textColor,
        )
    }
}

@Composable
fun CareDialogButton(
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

@Composable
fun CareFloatingButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color = CareTheme.colors.gray700,
    textColor: Color = CareTheme.colors.white000,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    enable: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enable,
        shape = RoundedCornerShape(50.dp),
        border = border,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
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
            style = CareTheme.typography.subtitle3,
            color = textColor,
        )
    }
}

@Composable
private fun PreviewButtonLarge() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        CareButtonLarge(text = "다음", onClick = {}, modifier = Modifier.fillMaxWidth())
        CareButtonLarge(
            text = "다음",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enable = false
        )
    }
}

@Composable
private fun PreviewButtonMedium() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        CareButtonMedium(text = "확인", onClick = {})
        CareButtonMedium(text = "확인", onClick = {}, enable = false)
    }
}

@Composable
private fun PreviewButtonCardLarge() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        CareButtonCardLarge(text = "저장하기", onClick = {}, modifier = Modifier.fillMaxWidth())
        CareButtonCardLarge(
            text = "저장하기",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enable = false
        )
    }
}

@Composable
private fun PreviewButtonCardMedium() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        CareButtonCardMedium(text = "저장하기", onClick = {})
        CareButtonCardMedium(text = "저장하기", onClick = {}, enable = false)
    }
}

@Composable
private fun PreviewButtonRound() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        CareButtonRound(text = "삭제하기", onClick = {})
        CareButtonRound(text = "삭제하기", onClick = {}, enable = false)
    }
}

@Composable
private fun PreviewButtonLine() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        CareButtonLine(text = "삭제하기", onClick = {})
        CareButtonLine(text = "삭제하기", onClick = {}, enable = false)
    }
}

@Composable
private fun PreviewButtonDialog() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        CareDialogButton(
            text = "Dialog",
            onClick = {},
            containerColor = CareTheme.colors.orange500,
            textColor = CareTheme.colors.white000
        )
        CareDialogButton(
            text = "Dialog",
            onClick = {},
            containerColor = CareTheme.colors.orange500,
            textColor = CareTheme.colors.white000
        )
    }
}

@Composable
private fun PreviewFloatingButton() {
    CareFloatingButton(text = "+ 공고 등록", onClick = {})
}

// Default Group Previews
@Preview(name = "Button_Primary_Default_Large", showBackground = true, group = "Default")
@Composable
private fun PreviewButtonPrimaryDefaultLarge() {
    PreviewButtonLarge()
}

@Preview(name = "Button_Primary_Default_Medium", showBackground = true, group = "Default")
@Composable
private fun PreviewButtonPrimaryDefaultMedium() {
    PreviewButtonMedium()
}

@Preview(name = "Button_Primary_Default_CardLarge", showBackground = true, group = "Default")
@Composable
private fun PreviewButtonPrimaryDefaultCardLarge() {
    PreviewButtonCardLarge()
}

@Preview(name = "Button_Primary_Default_CardMedium", showBackground = true, group = "Default")
@Composable
private fun PreviewButtonPrimaryDefaultCardMedium() {
    PreviewButtonCardMedium()
}

@Preview(name = "Button_Primary_Default_Round", showBackground = true, group = "Default")
@Composable
private fun PreviewButtonPrimaryDefaultRound() {
    PreviewButtonRound()
}

@Preview(name = "Button_Primary_Default_Line", showBackground = true, group = "Default")
@Composable
private fun PreviewButtonPrimaryDefaultLine() {
    PreviewButtonLine()
}

@Preview(name = "Button_Primary_Default_Dialog", showBackground = true, group = "Default")
@Composable
private fun PreviewButtonPrimaryDefaultDialog() {
    PreviewButtonDialog()
}

@Preview(name = "Button_Primary_Default_Floating", showBackground = true, group = "Default")
@Composable
private fun PreviewButtonPrimaryDefaultFloating() {
    PreviewFloatingButton()
}

// Flip Group Previews
@Preview(name = "Button_Primary_Flip_Large", showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewButtonPrimaryFlipLarge() {
    PreviewButtonLarge()
}

@Preview(name = "Button_Primary_Flip_Medium", showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewButtonPrimaryFlipMedium() {
    PreviewButtonMedium()
}

@Preview(
    name = "Button_Primary_Flip_CardLarge",
    showBackground = true,
    device = FLIP,
    group = "Flip"
)
@Composable
private fun PreviewButtonPrimaryFlipCardLarge() {
    PreviewButtonCardLarge()
}

@Preview(
    name = "Button_Primary_Flip_CardMedium",
    showBackground = true,
    device = FLIP,
    group = "Flip"
)
@Composable
private fun PreviewButtonPrimaryFlipCardMedium() {
    PreviewButtonCardMedium()
}

@Preview(
    name = "Button_Primary_Flip_Round",
    showBackground = true,
    device = FLIP,
    group = "Flip",
)
@Composable
private fun PreviewButtonPrimaryFlipRound() {
    PreviewButtonRound()
}

@Preview(
    name = "Button_Primary_Flip_Line",
    showBackground = true,
    device = FLIP,
    group = "Flip"
)
@Composable
private fun PreviewButtonPrimaryFlipLine() {
    PreviewButtonLine()
}

@Preview(name = "Button_Primary_Flip_Dialog", showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewButtonPrimaryFlipDialog() {
    PreviewButtonDialog()
}

@Preview(
    name = "Button_Primary_Flip_Floating",
    showBackground = true,
    device = FLIP,
    group = "Flip",
)
@Composable
private fun PreviewButtonPrimaryFlipFloating() {
    PreviewFloatingButton()
}

// Fold Group Previews
@Preview(
    name = "Button_Primary_Foldable_Large",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewButtonPrimaryFoldableLarge() {
    PreviewButtonLarge()
}

@Preview(
    name = "Button_Primary_Foldable_Medium",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewButtonPrimaryFoldableMedium() {
    PreviewButtonMedium()
}

@Preview(
    name = "Button_Primary_Foldable_CardLarge",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewButtonPrimaryFoldableCardLarge() {
    PreviewButtonCardLarge()
}

@Preview(
    name = "Button_Primary_Foldable_CardMedium",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewButtonPrimaryFoldableCardMedium() {
    PreviewButtonCardMedium()
}

@Preview(
    name = "Button_Primary_Foldable_Round",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewButtonPrimaryFoldableRound() {
    PreviewButtonRound()
}

@Preview(
    name = "Button_Primary_Foldable_Line",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewButtonPrimaryFoldableLine() {
    PreviewButtonLine()
}

@Preview(
    name = "Button_Primary_Foldable_Dialog",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)

@Composable
private fun PreviewButtonPrimaryFoldableDialog() {
    PreviewButtonDialog()
}

@Preview(
    name = "Button_Primary_Flip_Floating",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewButtonPrimaryFoldableFloating() {
    PreviewFloatingButton()
}