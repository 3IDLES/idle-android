package com.idle.signup.center.step

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.idle.center.jobposting.JobPostingStep
import com.idle.center.jobposting.JobPostingStep.ADDITIONAL_INFO
import com.idle.center.jobposting.JobPostingStep.CUSTOMER_REQUIREMENT
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
internal fun AdditionalInfoScreen(
    setJobPostingStep: (JobPostingStep) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    BackHandler { setJobPostingStep(JobPostingStep.findStep(ADDITIONAL_INFO.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier.fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 30.dp),
    ) {
        Text(
            text = "추가 지원정보를 입력해주세요.",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        LabeledContent(
            subtitle = "경력 우대 여부"
        ) {}

        LabeledContent(
            subtitle = "지원 방법"
        ) {}

        LabeledContent(
            subtitle = "접수 마감일"
        ) {}

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "다음",
            enable = true,
            onClick = { setJobPostingStep(JobPostingStep.findStep(ADDITIONAL_INFO.step + 1)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}