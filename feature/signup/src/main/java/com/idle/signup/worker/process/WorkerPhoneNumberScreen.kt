package com.idle.signup.worker.process

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idle.signin.worker.WorkerSignUpProcess

@Composable
internal fun WorkerPhoneNumberScreen(
    workerPhoneNumber: String,
    workerCertificationNumber: String,
    onWorkerPhoneNumberChanged: (String) -> Unit,
    onWorkerCertificationNumberChanged: (String) -> Unit,
    setSignUpProcess: (WorkerSignUpProcess) -> Unit,
    sendPhoneNumber: () -> Unit,
    confirmAuthCode: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "전화번호를 입력해주세요")

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(text = "전화번호")

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = workerPhoneNumber,
                    onValueChange = onWorkerPhoneNumberChanged
                )

                Button(onClick = sendPhoneNumber) {
                    Text(text = "인증")
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(text = "인증번호")

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = workerCertificationNumber,
                    onValueChange = onWorkerCertificationNumberChanged
                )

                Button(onClick = confirmAuthCode) {
                    Text(text = "확인")
                }
            }
        }

        Button(onClick = { setSignUpProcess(WorkerSignUpProcess.NAME) }) {
            Text(text = "다음")
        }
    }
}