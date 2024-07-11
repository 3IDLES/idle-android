package com.idle.signup.worker.process

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idle.signin.worker.WorkerSignUpProcess

@Composable
internal fun AddressScreen(
    onAddressDetailChanged: (String) -> Unit,
    setSignUpProcess: (WorkerSignUpProcess) -> Unit,
) {
    BackHandler { setSignUpProcess(WorkerSignUpProcess.NAME) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "현재 거주 중인 곳의 \n주소를 입력해주세요.")

        Button(onClick = { }) {
            Text(text = "가입완료")
        }
    }
}