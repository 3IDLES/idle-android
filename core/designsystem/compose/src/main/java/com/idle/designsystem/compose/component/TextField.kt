package com.idle.designsystem.compose.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.FLIP
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    supportingText: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    isError: Boolean = false,
    onDone: () -> Unit = {},
    textStyle: TextStyle = CareTheme.typography.body3.copy(
        color = if (readOnly) {
            CareTheme.colors.gray300
        } else {
            CareTheme.colors.gray900
        },
    ),
    leftComponent: @Composable () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val boarderStroke = BorderStroke(
        width = 1.dp,
        color = if (isError) {
            CareTheme.colors.red
        } else if (isFocused) {
            CareTheme.colors.gray700
        } else {
            CareTheme.colors.gray100
        },
    )

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
                    border = boarderStroke,
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 16.dp),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChanged,
                textStyle = textStyle,
                singleLine = true,
                readOnly = readOnly,
                enabled = enabled,
                interactionSource = interactionSource,
                visualTransformation = visualTransformation,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    onDone()
                }),
                modifier = Modifier
                    .weight(1f)
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

@Composable
fun CareTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    isError: Boolean = false,
    onDone: () -> Unit = {},
    textStyle: TextStyle = CareTheme.typography.body3.copy(
        color = if (readOnly) {
            CareTheme.colors.gray300
        } else {
            CareTheme.colors.gray900
        },
    ),
    leftComponent: @Composable () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val boarderStroke = BorderStroke(
        width = 1.dp,
        color = if (isError) {
            CareTheme.colors.red
        } else if (isFocused) {
            CareTheme.colors.gray700
        } else {
            CareTheme.colors.gray100
        },
    )

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
                    border = boarderStroke,
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 16.dp),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChanged,
                textStyle = textStyle,
                singleLine = true,
                readOnly = readOnly,
                enabled = enabled,
                interactionSource = interactionSource,
                visualTransformation = visualTransformation,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    onDone()
                }),
                modifier = Modifier
                    .weight(1f)
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
    }
}

@Composable
fun CareTextFieldLong(
    value: String,
    hint: String = "",
    readOnly: Boolean = false,
    enabled: Boolean = true,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChanged: (String) -> Unit,
    onDone: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val boarderStroke = BorderStroke(
        width = 1.dp,
        color = if (isError) {
            CareTheme.colors.red
        } else if (isFocused) {
            CareTheme.colors.gray700
        } else {
            CareTheme.colors.gray100
        },
    )

    Box(
        modifier = modifier
            .height(156.dp)
            .background(
                color = if (readOnly) {
                    CareTheme.colors.gray050
                } else {
                    CareTheme.colors.white000
                }, shape = RoundedCornerShape(6.dp)
            )
            .border(
                border = boarderStroke,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(vertical = 12.dp, horizontal = 16.dp),
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
            readOnly = readOnly,
            enabled = enabled,
            interactionSource = interactionSource,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                onDone()
                keyboardController?.hide()
            }),
            modifier = Modifier.fillMaxSize(),
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
    }
}

@Composable
fun CareClickableTextField(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    leftComponent: @Composable () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(color = CareTheme.colors.white000, shape = RoundedCornerShape(6.dp))
            .border(
                border = BorderStroke(width = 1.dp, color = CareTheme.colors.gray100),
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = value,
            style = CareTheme.typography.body3,
            color = CareTheme.colors.gray900,
        )

        if (value.isBlank()) {
            Text(
                text = hint,
                style = CareTheme.typography.body3,
                color = CareTheme.colors.gray300,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        leftComponent()
    }
}

@Composable
private fun CareTextFieldDefaultContent() {
    CareTextField(
        value = "Sample Text",
        onValueChanged = {},
        supportingText = "Supporting text",
        hint = "Hint",
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun CareTextFieldLongDefaultContent() {
    CareTextFieldLong(
        value = "Sample Long Text",
        hint = "Long Hint",
        onValueChanged = {},
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun CareClickableTextFieldDefaultContent() {
    CareClickableTextField(
        value = "Clickable Text",
        onClick = {},
        leftComponent = {
            Image(
                painter = painterResource(R.drawable.ic_arrow_down),
                contentDescription = null,
            )
        },
        hint = "Hint",
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(name = "TextField_Default", showBackground = true, group = "Default")
@Composable
private fun PreviewCareTextFieldDefault() {
    CareTextFieldDefaultContent()
}

@Preview(name = "TextField_Flip", showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewCareTextFieldFlip() {
    CareTextFieldDefaultContent()
}

@Preview(
    name = "TextField_Foldable",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareTextFieldFoldable() {
    CareTextFieldDefaultContent()
}

@Preview(name = "TextFieldLong_Default", showBackground = true, group = "Default")
@Composable
private fun PreviewCareTextFieldLongDefault() {
    CareTextFieldLongDefaultContent()
}

@Preview(name = "TextFieldLong_Flip", showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewCareTextFieldLongFlip() {
    CareTextFieldLongDefaultContent()
}

@Preview(
    name = "TextFieldLong_Foldable",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareTextFieldLongFoldable() {
    CareTextFieldLongDefaultContent()
}

@Preview(name = "ClickableTextField_Default", showBackground = true, group = "Default")
@Composable
private fun PreviewCareClickableTextFieldDefault() {
    CareClickableTextFieldDefaultContent()
}

@Preview(name = "ClickableTextField_Flip", showBackground = true, device = FLIP, group = "Flip")
@Composable
private fun PreviewCareClickableTextFieldFlip() {
    CareClickableTextFieldDefaultContent()
}

@Preview(
    name = "ClickableTextField_Foldable",
    showBackground = true,
    device = Devices.FOLDABLE,
    group = "Fold"
)
@Composable
private fun PreviewCareClickableTextFieldFoldable() {
    CareClickableTextFieldDefaultContent()
}
