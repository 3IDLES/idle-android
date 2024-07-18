package com.idle.signup.worker.process

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareChip
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.worker.Gender
import com.idle.signin.worker.WorkerSignUpProcess

@Composable
internal fun GenderScreen(
    gender: Gender,
    phoneNumberProcessed: Boolean,
    onGenderChanged: (Gender) -> Unit,
    setSignUpProcess: (WorkerSignUpProcess) -> Unit,
    setPhoneNumberProcessed: (Boolean) -> Unit,
) {
    BackHandler { setSignUpProcess(WorkerSignUpProcess.NAME) }

    LaunchedEffect(gender) {
        if (gender != Gender.NONE && !phoneNumberProcessed) {
            setSignUpProcess(WorkerSignUpProcess.PHONE_NUMBER)
            setPhoneNumberProcessed(true)
        }
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "본인의 성별을 선택해주세요",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            CareChip(
                text = Gender.FEMALE.displayName,
                onClick = { onGenderChanged(Gender.FEMALE) },
                enable = gender == Gender.FEMALE,
                modifier = Modifier.weight(1f),
            )

            CareChip(
                text = Gender.MALE.displayName,
                onClick = { onGenderChanged(Gender.MALE) },
                enable = gender == Gender.MALE,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "다음",
            enable = gender != Gender.NONE,
            onClick = { setSignUpProcess(WorkerSignUpProcess.PHONE_NUMBER) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}