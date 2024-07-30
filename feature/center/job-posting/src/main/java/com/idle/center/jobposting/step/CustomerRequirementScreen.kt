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
import com.idle.center.jobposting.JobPostingStep.CUSTOMER_REQUIREMENT
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareTextFieldLong
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
internal fun CustomerRequirementScreen(
    setJobPostingStep: (JobPostingStep) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    BackHandler { setJobPostingStep(JobPostingStep.findStep(CUSTOMER_REQUIREMENT.step - 1)) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier.fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 30.dp),
    ) {
        Text(
            text = "고객 요구사항을 입력해주세요.",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        LabeledContent(
            subtitle = "식사보조"
        ) {}

        LabeledContent(
            subtitle = "배변보조"
        ) {}

        LabeledContent(
            subtitle = "이동보조"
        ) {}

        LabeledContent(
            subtitle = "일상보조"
        ) {}

        LabeledContent(
            subtitle = "이외에 요구사항이 있다면 적어주세요."
        ) {
            CareTextFieldLong(
                value = "",
                hint = "추가적으로 요구사항이 있다면 작성해주세요.(예: 어쩌고저쩌고)",
                onValueChanged = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "다음",
            enable = true,
            onClick = { setJobPostingStep(JobPostingStep.findStep(CUSTOMER_REQUIREMENT.step + 1)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}