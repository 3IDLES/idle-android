package com.idle.chatting_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareSnackBar
import com.idle.designsystem.compose.component.CareSubtitleTopBar
import com.idle.designsystem.compose.foundation.CareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class ChattingDetailFragment : BaseComposeFragment() {
    private val args: ChattingDetailFragmentArgs by navArgs()
    override val fragmentViewModel: ChattingDetailViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val chattingRoomId = args.chattingRoomId
            val userId = args.userId
            val writingText by writingText.collectAsStateWithLifecycle()

            ChattingDetailScreen(
                snackbarHostState = snackbarHostState,
                writingText = writingText,
                onWritingTextChange = ::setWritingText,
                navigateUp = { findNavController().navigateUp() },
            )
        }
    }
}

@Composable
internal fun ChattingDetailScreen(
    snackbarHostState: SnackbarHostState,
    writingText: String,
    onWritingTextChange: (String) -> Unit,
    navigateUp: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            CareSubtitleTopBar(
                title = "세얼간이요양센터",
                onNavigationClick = navigateUp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp, end = 20.dp, bottom = 12.dp),
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    CareSnackBar(
                        data = data,
                        modifier = Modifier.padding(bottom = 84.dp)
                    )
                }
            )
        },
        containerColor = CareTheme.colors.white000,
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(CareTheme.colors.gray050),
            ) {

            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 28.dp, top = 20.dp, bottom = 50.dp)
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
                    modifier = Modifier.size(32.dp),
                )
            }
        }
    }
}

@Composable
fun CareChatTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onDone: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    val boarderStroke = BorderStroke(
        width = 1.dp,
        color = CareTheme.colors.gray100,
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .wrapContentHeight()
            .border(
                border = boarderStroke,
                shape = RoundedCornerShape(50.dp)
            )
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChanged,
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
                .padding(horizontal = 20.dp, vertical = 7.dp),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = hint,
                        style = CareTheme.typography.body3,
                        color = CareTheme.colors.gray300,
                    )
                }

                innerTextField()
            }
        )
    }
}