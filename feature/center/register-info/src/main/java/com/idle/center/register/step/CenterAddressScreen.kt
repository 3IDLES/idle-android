package com.idle.signup.center.step

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idle.center.register.RegistrationStep
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareClickableTextField
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
internal fun CenterAddressScreen(
    roadNameAddress: String,
    centerDetailAddress: String,
    navigateToPostCode: () -> Unit,
    onCenterDetailAddressChanged: (String) -> Unit,
    setRegistrationStep: (RegistrationStep) -> Unit,
) {
    BackHandler { setRegistrationStep(RegistrationStep.INFO) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier.fillMaxSize()
            .padding(bottom = 30.dp),
    ) {
        Text(
            text = "센터 주소 정보를 입력해주세요",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        LabeledContent(
            subtitle = "도로명 주소",
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareClickableTextField(
                value = roadNameAddress,
                hint = "도로명 주소를 입력해주세요.",
                onClick = navigateToPostCode,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        LabeledContent(
            subtitle = "상세 주소",
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareTextField(
                value = centerDetailAddress,
                hint = "상세 주소를 입력해주세요. (예: 2층 204호)",
                onValueChanged = onCenterDetailAddressChanged,
                onDone = {
                    if (roadNameAddress.isNotBlank() && centerDetailAddress.isNotBlank())
                        setRegistrationStep(RegistrationStep.INTRODUCE)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "다음",
            enable = centerDetailAddress.isNotBlank(),
            onClick = { setRegistrationStep(RegistrationStep.INTRODUCE) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}