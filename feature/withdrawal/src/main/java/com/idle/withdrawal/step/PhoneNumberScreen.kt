package com.idle.withdrawal.step

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.withdrawal.WithdrawalStep

@Composable
internal fun PhoneNumberScreen(
    setWithdrawalStep: (WithdrawalStep) -> Unit,
) {
    BackHandler { setWithdrawalStep(WithdrawalStep.findStep(WithdrawalStep.PHONENUMBER.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp),
    ) {
        Text(
            text = "마지막으로\n" +
                    "전화번호를 인증해주세요.",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )


        Spacer(modifier = Modifier.weight(1f))
    }
}