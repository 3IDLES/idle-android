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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.idle.center.register.RegistrationStep
import com.idle.center.register.RegistrationStep.ADDRESS
import com.idle.center.register.RegistrationStep.INFO
import com.idle.center.register.RegistrationStep.INTRODUCE
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareClickableTextField
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
internal fun CenterAddressScreen(
    roadNameAddress: String,
    centerDetailAddress: String,
    navigateToPostCode: () -> Unit,
    onCenterDetailAddressChanged: (String) -> Unit,
    setRegistrationStep: (RegistrationStep) -> Unit,
) {
    BackHandler { setRegistrationStep(RegistrationStep.findStep(ADDRESS.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp),
    ) {
        Text(
            text = stringResource(id = R.string.center_info_input),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        LabeledContent(
            subtitle = stringResource(id = R.string.road_name_address),
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareClickableTextField(
                value = roadNameAddress,
                hint = stringResource(id = R.string.road_name_address_hint),
                onClick = navigateToPostCode,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        LabeledContent(
            subtitle = stringResource(id = R.string.detail_address),
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareTextField(
                value = centerDetailAddress,
                hint = stringResource(id = R.string.detail_address_hint),
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
            text = stringResource(id = R.string.next),
            enable = centerDetailAddress.isNotBlank(),
            onClick = { setRegistrationStep(RegistrationStep.findStep(ADDRESS.step + 1)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}