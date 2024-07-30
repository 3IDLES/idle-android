package com.idle.signup.worker.step

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.worker.WorkerSignUpStep

@Composable
internal fun AddressScreen(
    address: String,
    addressDetail: String,
    onAddressChanged: (String) -> Unit,
    onAddressDetailChanged: (String) -> Unit,
    setSignUpStep: (WorkerSignUpStep) -> Unit,
    signUpWorker: () -> Unit,
) {
    BackHandler { setSignUpStep(WorkerSignUpStep.PHONE_NUMBER) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "현재 거주 중인 곳의 \n주소를 입력해주세요.",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = "우편번호",
                style = CareTheme.typography.subtitle4,
                color = CareTheme.colors.gray500,
            )

            CareTextField(
                value = address,
                hint = "우편번호를 입력해주세요. (예: 14354)",
                onValueChanged = onAddressChanged,
                onDone = { },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = "상세 주소",
                style = CareTheme.typography.subtitle4,
                color = CareTheme.colors.gray500,
            )

            CareTextField(
                value = addressDetail,
                hint = "상세 주소를 입력해주세요. (예: 101동 101호)",
                onValueChanged = onAddressDetailChanged,
                onDone = { },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "완료",
            enable = addressDetail.isNotBlank(),
            onClick = signUpWorker,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}