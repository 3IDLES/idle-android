package com.idle.designsystem.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareTextField(
    value: String,
    hint: String,
    readOnly: Boolean = false,
    isError: Boolean = false,
    supportingText: String = "",
    onValueChanged: (String) -> Unit,
    onDone: () -> Unit = {},
    leftComponent: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(44.dp)
                .background(
                    color = if (readOnly) {
                        CareTheme.colors.gray050
                    } else {
                        CareTheme.colors.white000
                    }, shape = RoundedCornerShape(6.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (isError) {
                        CareTheme.colors.red
                    } else {
                        CareTheme.colors.gray100
                    },
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 16.dp),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChanged,
                textStyle = CareTheme.typography.body3.copy(
                    color = if (readOnly) {
                        CareTheme.colors.gray300
                    } else {
                        CareTheme.colors.gray900
                    },
                ),
                singleLine = true,
                readOnly = readOnly,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    onDone()
                    keyboardController?.hide()
                }),
                modifier = Modifier.weight(1f)
                    .padding(top = 10.dp, bottom = 10.dp, end = 8.dp),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            style = CareTheme.typography.body3,
                            color = CareTheme.colors.gray200,
                        )
                    }
                    innerTextField()
                }
            )

            leftComponent()
        }

        if (supportingText.isNotBlank()) {
            Text(
                text = supportingText,
                style = CareTheme.typography.caption,
                color = if (isError) {
                    CareTheme.colors.red
                } else {
                    CareTheme.colors.gray300
                },
            )
        }
    }
}