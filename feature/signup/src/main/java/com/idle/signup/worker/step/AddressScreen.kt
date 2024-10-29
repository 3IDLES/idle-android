package com.idle.signup.worker.step

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
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonMedium
import com.idle.designsystem.compose.component.CareClickableTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.worker.WorkerSignUpStep
import com.idle.signin.worker.WorkerSignUpStep.ADDRESS
import com.idle.signup.LogWorkerSignUpStep

@Composable
internal fun AddressScreen(
    roadNameAddress: String,
    showPostCode: () -> Unit,
    setSignUpStep: (WorkerSignUpStep) -> Unit,
    signUpWorker: () -> Unit,
) {
    BackHandler { setSignUpStep(WorkerSignUpStep.findStep(ADDRESS.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.worker_address_title),
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.black,
            modifier = Modifier.padding(bottom = 28.dp),
        )

        LabeledContent(
            subtitle = stringResource(id = R.string.road_name_address),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
        ) {
            CareClickableTextField(
                value = roadNameAddress,
                hint = stringResource(id = R.string.road_name_address_hint),
                onClick = showPostCode,
                modifier = Modifier.fillMaxWidth(),
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
                onClick = { setSignUpStep(WorkerSignUpStep.findStep(ADDRESS.step - 1)) },
                modifier = Modifier.weight(1f),
            )

            CareButtonMedium(
                text = stringResource(id = R.string.complete),
                enable = roadNameAddress.isNotBlank(),
                onClick = signUpWorker,
                modifier = Modifier.weight(1f),
            )
        }
    }

    LogWorkerSignUpStep(ADDRESS)
}