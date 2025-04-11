package com.idle.chatting_detail.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.idle.compose.clickable

@Composable
fun ChattingInput(
    writingText: String,
    onWritingTextChange: (String) -> Unit,
    sendMessage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 16.dp)
    ) {
        CareChatTextField(
            value = writingText,
            onValueChanged = onWritingTextChange,
            hint = "메세지를 입력하세요.",
            modifier = Modifier.weight(1f),
        )

        Image(
            painter = painterResource(com.idle.designresource.R.drawable.ic_send_message),
            contentDescription = "",
            modifier = Modifier
                .size(32.dp)
                .clickable(throttleTime = 1000L) { sendMessage() },
        )
    }
}

