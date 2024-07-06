package com.idle.signup.center.process

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
import com.idle.signin.center.CenterSignUpProcess

@Composable
internal fun NameScreen(
    centerName: String,
    onCenterNameChanged: (String) -> Unit,
    setSignUpProcess: (CenterSignUpProcess) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "성함을 입력해주세요")

        TextField(
            value = centerName,
            onValueChange = onCenterNameChanged
        )

        Button(onClick = { setSignUpProcess(CenterSignUpProcess.PHONE_NUMBER) }) {
            Text(text = "다음")
        }
    }
}