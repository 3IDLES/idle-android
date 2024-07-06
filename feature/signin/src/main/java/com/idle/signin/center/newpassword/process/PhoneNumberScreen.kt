package com.idle.signin.center.newpassword.process

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
import com.idle.signin.center.newpassword.GenerateNewPasswordProcess
import com.idle.signin.center.newpassword.GenerateNewPasswordProcess.GENERATE_NEW_PASSWORD

@Composable
internal fun PhoneNumberScreen(
    phoneNumber: String,
    certificationNumber: String,
    onPhoneNumberChanged: (String) -> Unit,
    onCertificationNumberChanged: (String) -> Unit,
    setGenerateNewPasswordProcess: (GenerateNewPasswordProcess) -> Unit,
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
                    value = phoneNumber,
                    onValueChange = onPhoneNumberChanged
                )

                Button(onClick = { }) {
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
                    value = certificationNumber,
                    onValueChange = onCertificationNumberChanged
                )

                Button(onClick = { }) {
                    Text(text = "확인")
                }
            }
        }

        Button(onClick = { setGenerateNewPasswordProcess(GENERATE_NEW_PASSWORD) }) {
            Text(text = "다음")
        }
    }
}