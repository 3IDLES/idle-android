package com.idle.signin.center.newpassword.process

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idle.signin.center.newpassword.GenerateNewPasswordProcess
import com.idle.signin.center.newpassword.GenerateNewPasswordProcess.PHONE_NUMBER

@Composable
internal fun GenerateNewPasswordScreen(
    newPassword: String,
    newPasswordForConfirm: String,
    onNewPasswordChanged: (String) -> Unit,
    onNewPasswordForConfirmChanged: (String) -> Unit,
    setGenerateNewPasswordProcess: (GenerateNewPasswordProcess) -> Unit,
) {
    BackHandler { setGenerateNewPasswordProcess(PHONE_NUMBER) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "새로운 비밀번호를 입력해주세요")

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(text = "비밀번호 설정 (영문+숫자 조합 10자리 이상 등 조건)")

            TextField(
                value = newPassword,
                onValueChange = onNewPasswordChanged
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(text = "비밀번호 확인")

            TextField(
                value = newPasswordForConfirm,
                onValueChange = onNewPasswordForConfirmChanged,
            )
        }

        Button(onClick = { }) {
            Text(text = "완료")
        }
    }
}