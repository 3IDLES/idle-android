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
internal fun IdPasswordScreen(
    centerId: String,
    centerPassword: String,
    centerPasswordForConfirm: String,
    onCenterIdChanged: (String) -> Unit,
    onCenterPasswordChanged: (String) -> Unit,
    onCenterPasswordForConfirmChanged: (String) -> Unit,
    setSignUpProcess: (CenterSignUpProcess) -> Unit
) {
    BackHandler { setSignUpProcess(CenterSignUpProcess.BUSINESS_REGISTRAION_NUMBER) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "아이디와 비밀번호를 입력해주세요")

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(text = "아이디 설정 (영문+숫자 조합 10자리 이상 등 조건)")

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = centerId,
                    onValueChange = onCenterIdChanged,
                )

                Button(onClick = { }) {
                    Text(text = "중복 확인")
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(text = "비밀번호 설정 (영문+숫자 조합 10자리 이상 등 조건)")

            TextField(
                value = centerPassword,
                onValueChange = onCenterPasswordChanged
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Text(text = "비밀번호 확인")

            TextField(
                value = centerPasswordForConfirm,
                onValueChange = onCenterPasswordForConfirmChanged,
            )
        }

        Button(onClick = { }) {
            Text(text = "완료")
        }
    }
}