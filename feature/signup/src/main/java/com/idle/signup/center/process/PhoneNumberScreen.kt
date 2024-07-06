package com.idle.signup.center.process

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
import com.idle.signin.center.CenterSignUpProcess

@Composable
internal fun PhoneNumberScreen(
    setSignUpProcess: (CenterSignUpProcess) -> Unit,
) {
    BackHandler { setSignUpProcess(CenterSignUpProcess.NAME) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "전화번호를 입력해주세요")

        Button(onClick = { setSignUpProcess(CenterSignUpProcess.BUSINESS_REGISTRAION_NUMBER) }) {
            Text(text = "다음")
        }
    }
}