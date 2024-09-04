package com.idle.designsystem.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idle.designresource.R
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareSnackBar(
    data: SnackbarData,
    modifier: Modifier = Modifier,
) {
    val (msg, type) = try {
        data.visuals.message.split("|").let {
            if (it.size >= 2) {
                it[0] to it[1]
            } else {
                data.visuals.message to SnackBarType.ERROR.name
            }
        }
    } catch (e: Exception) {
        data.visuals.message to SnackBarType.ERROR.name
    }

    val snackBarType = SnackBarType.create(type)

    val (backgroundColor, leftImage) = when (snackBarType) {
        SnackBarType.SUCCESS -> CareTheme.colors.gray500 to R.drawable.ic_check_gray
        SnackBarType.ERROR -> CareTheme.colors.red to R.drawable.ic_error
    }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Image(
            painter = painterResource(id = leftImage),
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp),
        )

        Text(
            text = msg,
            style = CareTheme.typography.subtitle4,
            color = CareTheme.colors.white000,
        )
    }
}

private enum class SnackBarType {
    ERROR, SUCCESS;

    companion object {
        fun create(type: String): SnackBarType {
            return SnackBarType.entries.firstOrNull { it.name == type } ?: ERROR
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CareSnackBarPreview() {
    CareSnackBar(
        data = MockSnackbarData("전화번호를 다시 확인해주세요."),
        modifier = Modifier.fillMaxWidth(),
    )
}

private class MockSnackbarData(private val message: String) : SnackbarData {
    override val visuals: SnackbarVisuals
        get() = object : SnackbarVisuals {
            override val message: String
                get() = this@MockSnackbarData.message
            override val actionLabel: String? = null
            override val duration: SnackbarDuration = SnackbarDuration.Short
            override val withDismissAction: Boolean = true
        }

    override fun performAction() {}
    override fun dismiss() {}
}