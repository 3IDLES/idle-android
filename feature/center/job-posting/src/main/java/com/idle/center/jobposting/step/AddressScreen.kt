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
import com.idle.center.jobposting.JobPostingStep
import com.idle.center.jobposting.JobPostingStep.ADDRESS
import com.idle.designresource.R
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareClickableTextField
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
internal fun AddressScreen(
    roadNameAddress: String,
    detailAddress: String,
    showPostCodeDialog: () -> Unit,
    onDetailAddressChanged: (String) -> Unit,
    setJobPostingStep: (JobPostingStep) -> Unit,
) {
    BackHandler { setJobPostingStep(JobPostingStep.findStep(ADDRESS.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp),
    ) {
        Text(
            text = stringResource(id = R.string.address_screen_title),
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
                onClick = showPostCodeDialog,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        LabeledContent(
            subtitle = stringResource(id = R.string.detail_address),
            modifier = Modifier.fillMaxWidth(),
        ) {
            CareTextField(
                value = detailAddress,
                hint = stringResource(id = R.string.detail_address_hint),
                onValueChanged = onDetailAddressChanged,
                onDone = {
                    if (roadNameAddress.isNotBlank() && detailAddress.isNotBlank())
                        setJobPostingStep(JobPostingStep.findStep(ADDRESS.step + 1))
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = stringResource(id = R.string.next),
            enable = (roadNameAddress.isNotBlank() && detailAddress.isNotBlank()),
            onClick = { setJobPostingStep(JobPostingStep.findStep(ADDRESS.step + 1)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}