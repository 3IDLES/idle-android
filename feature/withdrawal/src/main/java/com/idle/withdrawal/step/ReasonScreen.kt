package com.idle.withdrawal.step

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonMedium
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.withdrawal.WithdrawalStep

@Composable
internal fun ReasonScreen(
    setWithdrawalStep: (WithdrawalStep) -> Unit,
) {
    val onBackPressedDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        Text(
            text = "정말 탈퇴하시겠어요?",
            style = CareTheme.typography.heading1,
            color = CareTheme.colors.gray900,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Text(
            text = "계정을 삭제하시려는 이유를 알려주세요.\n" +
                    "소중한 피드백을 받아 더 나은 서비스로 보답하겠습니다.",
            style = CareTheme.typography.body3,
            color = CareTheme.colors.gray300,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(5) {
                WithdrawalReasonItem(checked = true) {

                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "탈퇴 버튼 선택 시 모든 정보가 삭제되며, 되돌릴 수 없습니다.",
            style = CareTheme.typography.caption,
            color = CareTheme.colors.red,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth(),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            CareButtonMedium(
                text = stringResource(id = com.idle.designresource.R.string.cancel),
                textColor = CareTheme.colors.gray300,
                containerColor = CareTheme.colors.white000,
                border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray200),
                onClick = { onBackPressedDispatcher?.onBackPressed() },
                modifier = Modifier.weight(1f),
            )

            CareButtonMedium(
                text = stringResource(id = com.idle.designresource.R.string.withdrawal),
                textColor = CareTheme.colors.white000,
                containerColor = CareTheme.colors.red,
                onClick = { setWithdrawalStep(WithdrawalStep.findStep(WithdrawalStep.REASON.step + 1)) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun WithdrawalReasonItem(
    checked: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (!checked) CareTheme.colors.white000
        else CareTheme.colors.orange500,
    )
    val borderColor by animateColorAsState(
        targetValue = if (!checked) CareTheme.colors.gray100
        else CareTheme.colors.orange500
    )

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .size(20.dp)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(2.dp),
                )
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(2.dp),
                )
                .clickable { onClick() }
        ) {
            AnimatedVisibility(visible = checked) {
                Image(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = null
                )
            }
        }

        Text(
            text = "탈퇴하는 이유 어쩌고저쩌고",
            style = CareTheme.typography.body2,
            color = CareTheme.colors.gray500,
        )
    }
}