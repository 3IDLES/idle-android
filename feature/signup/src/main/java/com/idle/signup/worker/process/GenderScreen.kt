package com.idle.signup.worker.process

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idle.signin.worker.Gender
import com.idle.signin.worker.WorkerSignUpProcess

@Composable
internal fun GenderScreen(
    gender: Gender,
    onGenderChanged: (Gender) -> Unit,
    setSignUpProcess: (WorkerSignUpProcess) -> Unit,
) {
    BackHandler { setSignUpProcess(WorkerSignUpProcess.NAME) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "성별을 알려주세요")

        Row {
            Button(onClick = { onGenderChanged(Gender.MALE) }) {
                Text(text = "남성")
            }

            Button(onClick = { onGenderChanged(Gender.FEMALE) }) {
                Text(text = "여성")
            }
        }

        Button(onClick = { }) {
            Text(text = "가입완료")
        }
    }
}