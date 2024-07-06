package com.idle.signup.center.process

import androidx.activity.compose.BackHandler
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
import com.idle.signin.center.CenterSignUpProcess

@Composable
internal fun BusinessRegistrationScreen(
    businessRegistrationNumber: String,
    onBusinessRegistrationNumberChanged: (String) -> Unit,
    setSignUpProcess: (CenterSignUpProcess) -> Unit,
) {
    BackHandler { setSignUpProcess(CenterSignUpProcess.PHONE_NUMBER) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "사업자 등록번호를 입력해주세요")

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = businessRegistrationNumber,
                onValueChange = onBusinessRegistrationNumberChanged
            )

            Button(onClick = { }) {
                Text(text = "검색")
            }
        }

        Button(onClick = { setSignUpProcess(CenterSignUpProcess.ID_PASSWORD) }) {
            Text(text = "다음")
        }
    }
}