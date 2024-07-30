package com.idle.signup.center.step

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.component.CareButtonLarge
import com.idle.designsystem.compose.component.CareButtonSmall
import com.idle.designsystem.compose.component.CareTextField
import com.idle.designsystem.compose.component.LabeledContent
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.BusinessRegistrationInfo
import com.idle.signin.center.CenterSignUpStep

@Composable
internal fun BusinessRegistrationScreen(
    businessRegistrationNumber: String,
    businessRegistrationInfo: BusinessRegistrationInfo?,
    onBusinessRegistrationNumberChanged: (String) -> Unit,
    validateBusinessRegistrationNumber: () -> Unit,
    setSignUpStep: (CenterSignUpStep) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler { setSignUpStep(CenterSignUpStep.PHONE_NUMBER) }

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
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareTextField(
                    value = businessRegistrationNumber,
                    hint = "사업자 등록번호를 입력해주세요.",
                    onValueChanged = onBusinessRegistrationNumberChanged,
                    onDone = {
                        if (businessRegistrationNumber.isNotBlank()) {
                            validateBusinessRegistrationNumber()
                        }
                    },
                    modifier = Modifier.weight(1f)
                        .focusRequester(focusRequester),
                )

                CareButtonSmall(
                    enable = businessRegistrationNumber.isNotBlank(),
                    text = "검색",
                    onClick = {
                        validateBusinessRegistrationNumber()
                        keyboardController?.hide()
                    },
                )
            }
        }

        if (businessRegistrationInfo != null) {
            LabeledContent(
                subtitle = "아래의 시설이 맞나요?",
                modifier = Modifier.fillMaxWidth(),
            ) {
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
                            space = 4.dp,
                            alignment = Alignment.CenterVertically
                        ),
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        Text(
                            businessRegistrationInfo.companyName,
                            style = CareTheme.typography.subtitle3,
                            color = CareTheme.colors.gray900,
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                painter = painterResource(com.idle.designresource.R.drawable.ic_address_pin),
                                contentDescription = null,
                            )

                            Text(
                                text = businessRegistrationInfo.businessRegistrationNumber,
                                style = CareTheme.typography.body3,
                                color = CareTheme.colors.gray900,
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CareButtonLarge(
            text = "다음",
            enable = businessRegistrationInfo != null,
            onClick = { setSignUpStep(CenterSignUpStep.ID_PASSWORD) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}