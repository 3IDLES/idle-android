package com.idle.withdrawal.step

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.withdrawal.WithdrawalStep

@Composable
internal fun ReasonScreen(
    setWithdrawalStep: (WithdrawalStep) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
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

        Spacer(modifier = Modifier.weight(1f))
    }
}