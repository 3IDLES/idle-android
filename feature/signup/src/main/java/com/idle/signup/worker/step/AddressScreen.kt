package com.idle.signup.worker.step

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
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareClickableTextField
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.worker.WorkerSignUpStep
import com.idle.signin.worker.WorkerSignUpStep.ADDRESS

@Composable
internal fun AddressScreen(
    roadNameAddress: String,
    addressDetail: String,
    showPostCode: () -> Unit,
    onAddressDetailChanged: (String) -> Unit,
    setSignUpStep: (WorkerSignUpStep) -> Unit,
    signUpWorker: () -> Unit,
) {
    BackHandler { setSignUpStep(WorkerSignUpStep.findStep(ADDRESS.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.worker_address_title),
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
                onClick = showPostCode,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        LabeledContent(
            subtitle = stringResource(id = R.string.detail_address),
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareTextField(
                value = addressDetail,
                hint = stringResource(id = R.string.detail_address_hint),
                onValueChanged = onAddressDetailChanged,
                onDone = { if (roadNameAddress.isNotBlank() && addressDetail.isNotBlank()) signUpWorker() },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.complete),
            enable = roadNameAddress.isNotBlank() && addressDetail.isNotBlank(),
            onClick = signUpWorker,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }
}