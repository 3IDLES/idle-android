package com.idle.center.register.step

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.idle.center.register.LogRegistrationStep
import com.idle.center.register.RegistrationStep
import com.idle.center.register.RegistrationStep.ADDRESS
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonMedium
import com.idle.designsystem.compose.component.CareClickableTextField
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
internal fun CenterAddressScreen(
    roadNameAddress: String,
    centerDetailAddress: String,
    showPostCode: () -> Unit,
    onCenterDetailAddressChanged: (String) -> Unit,
    setRegistrationStep: (RegistrationStep) -> Unit,
) {
    BackHandler { setRegistrationStep(RegistrationStep.findStep(ADDRESS.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = stringResource(id = R.string.center_info_input),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.black,
        )

        LabeledContent(
            subtitle = stringResource(id = R.string.road_name_address),
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareClickableTextField(
                value = roadNameAddress,
                hint = stringResource(id = R.string.road_name_address_hint),
                onClick = showPostCode,
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

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 28.dp),
        ) {
            CareButtonMedium(
                text = stringResource(id = R.string.previous),
                textColor = CareTheme.colors.gray300,
                containerColor = CareTheme.colors.white000,
                border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray200),
                onClick = { setRegistrationStep(RegistrationStep.findStep(ADDRESS.step - 1)) },
                modifier = Modifier.weight(1f),
            )

            CareButtonMedium(
                text = stringResource(id = R.string.next),
                enable = centerDetailAddress.isNotBlank(),
                onClick = { setRegistrationStep(RegistrationStep.findStep(ADDRESS.step + 1)) },
                modifier = Modifier.weight(1f),
            )
        }
    }

    LogRegistrationStep(step = ADDRESS)
}