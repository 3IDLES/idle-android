package com.idle.signup.center.process

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
import com.idle.center.register.RegisterProcess
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareClickableTextField
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
internal fun CenterAddressScreen(
    centerAddress: String,
    centerDetailAddress: String,
    navigateToPostCode: () -> Unit,
    onCenterDetailAddressChanged: (String) -> Unit,
    setRegisterProcess: (RegisterProcess) -> Unit,
) {
    BackHandler { setRegisterProcess(RegisterProcess.INFO) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "센터 주소 정보를 입력해주세요",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = "도로명 주소",
                style = CareTheme.typography.subtitle4,
                color = CareTheme.colors.gray500,
            )

            CareClickableTextField(
                value = centerAddress,
                hint = "도로명 주소를 입력해주세요.",
                onClick = navigateToPostCode,
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
                value = centerDetailAddress,
                hint = "상세 주소를 입력해주세요. (예: 2층 204호)",
                onValueChanged = onCenterDetailAddressChanged,
                onDone = {
                    if (centerAddress.isNotBlank() && centerDetailAddress.isNotBlank())
                        setRegisterProcess(RegisterProcess.INTRODUCE)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "다음",
            enable = centerDetailAddress.isNotBlank(),
            onClick = { setRegisterProcess(RegisterProcess.INTRODUCE) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}