package com.idle.designsystem.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareTextField(
    value: String,
    hint: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = value,
        onValueChange = onValueChanged,
        textStyle = CareTheme.typography.body3,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
    ) { innerTextField ->
        Box(
            modifier = modifier
                .background(color = CareTheme.colors.white000, shape = RoundedCornerShape(6.dp))
                .border(
                    width = 1.dp,
                    color = CareTheme.colors.gray100,
                    shape = RoundedCornerShape(6.dp)
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 16.dp),
            ) {
                innerTextField()

                if (value.isEmpty()) {
                    Text(
                        text = hint,
                        style = CareTheme.typography.body3,
                        color = CareTheme.colors.gray200,
                    )
                }
            }
        }
    }
}