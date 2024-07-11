package com.idle.signup.center.process

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareButtonSmall
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.signin.center.CenterSignUpProcess

@Composable
internal fun BusinessRegistrationScreen(
    businessRegistrationNumber: String,
    onBusinessRegistrationNumberChanged: (String) -> Unit,
    setSignUpProcess: (CenterSignUpProcess) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var forTest by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler { setSignUpProcess(CenterSignUpProcess.PHONE_NUMBER) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "사업자 등록번호를 입력해주세요",
            style = CareTheme.typography.heading2,
            color = CareTheme.colors.gray900,
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareTextField(
                    value = businessRegistrationNumber,
                    hint = "사업자 등록번호를 입력해주세요.",
                    onValueChanged = onBusinessRegistrationNumberChanged,
                    onDone = { forTest = true },
                    modifier = Modifier.weight(1f)
                        .focusRequester(focusRequester),
                )

                CareButtonSmall(
                    enable = businessRegistrationNumber.isNotBlank(),
                    text = "검색",
                    onClick = {
                        forTest = true
                        keyboardController?.hide()
                    },
                )
            }
        }

        if (forTest) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
            ) {
                Text(
                    text = "아래의 시설이 맞나요?",
                    style = CareTheme.typography.subtitle3,
                    color = CareTheme.colors.gray500,
                )

                Box(
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = CareTheme.colors.gray100,
                        shape = RoundedCornerShape(8.dp),
                    ),
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(
                            4.dp,
                            Alignment.CenterVertically
                        ),
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(
                            "세얼간이 요양보호소",
                            style = CareTheme.typography.subtitle3,
                            color = CareTheme.colors.gray900,
                        )

                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = CareTheme.colors.gray300)) {
                                    append("주소  ")
                                }
                                withStyle(style = SpanStyle(color = CareTheme.colors.gray900)) {
                                    append("용인시 어쩌고 저쩌고")
                                }
                            },
                            style = CareTheme.typography.body3,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "다음",
            enable = forTest,
            onClick = { setSignUpProcess(CenterSignUpProcess.ID_PASSWORD) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}