package com.idle.worker.chatting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.idle.binding.DeepLinkDestination
import com.idle.binding.NavigationEvent
import com.idle.compose.base.BaseComposeFragment
import com.idle.compose.clickable
import com.idle.designsystem.compose.component.CareHeadingTopBar
import com.idle.designsystem.compose.component.LoadingCircle
import com.idle.designsystem.compose.foundation.CareTheme
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chatting.ChatRoom
import com.idle.domain.util.formatRelativeDateTime
import com.idle.domain.util.formatUnReadNumber
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class WorkerChattingFragment : BaseComposeFragment() {
    override val fragmentViewModel: WorkerChattingViewModel by viewModels()

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val chatRoomList by chatRoomList.collectAsStateWithLifecycle()

            LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
                getChatRoomList()
                subscribeChatMessage()
            }

            if (chatRoomList != null) {
                WorkerChattingScreen(
                    chatRoomList = chatRoomList,
                    navigateTo = { navigationHelper.navigateTo(NavigationEvent.NavigateTo(it)) },
                )
            } else {
                // Todo : 스켈레톤 UI 혹은 스피너 로딩
            }
        }
    }
}

@Composable
internal fun WorkerChattingScreen(
    chatRoomList: List<ChatRoom>?,
    navigateTo: (DeepLinkDestination) -> Unit,
) {
    Scaffold(
        topBar = {
            CareHeadingTopBar(
                title = stringResource(id = com.idle.designresource.R.string.chatting),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 8.dp),
            )
        },
        containerColor = CareTheme.colors.white000,
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .padding(paddingValue)
                .fillMaxSize()
        ) {
            chatRoomList?.let {
                if (chatRoomList.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(bottom = 60.dp),
                    ) {
                        Text(
                            text = "아직 채팅 내역이 없어요",
                            style = CareTheme.typography.heading2,
                            color = CareTheme.colors.black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                        )

                        Text(
                            text = "센터에 궁금한 점이 있다면 해당 공고에서\n" +
                                    "‘채팅하기’ 버튼을 눌러 채팅을 시작할 수 있어요.",
                            style = CareTheme.typography.body3,
                            textAlign = TextAlign.Center,
                            color = CareTheme.colors.gray300,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp),
                    ) {
                        items(
                            items = chatRoomList,
                            key = { it.id },
                        ) { chatRoom ->
                            ChatRoomItem(
                                chatRoom = chatRoom,
                                navigateTo = navigateTo,
                            )
                        }

                        item {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                            )
                        }
                    }
                }
            } ?: LoadingCircle(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
internal fun ChatRoomItem(
    chatRoom: ChatRoom,
    navigateTo: (DeepLinkDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (chatRoom.unReadMessageCount > 0) {
        CareTheme.colors.orange050
    } else {
        CareTheme.colors.white000
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable {
                navigateTo(
                    DeepLinkDestination.ChattingDetail(
                        chattingRoomId = chatRoom.id,
                        receiverId = chatRoom.receiver,
                        receiverUserType = UserType.WORKER.apiValue,
                        senderId = chatRoom.sender,
                    )
                )
            },
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            AsyncImage(
                model = chatRoom.profileImageUrl,
                placeholder = painterResource(com.idle.designresource.R.drawable.ic_notification_placeholder),
                error = painterResource(com.idle.designresource.R.drawable.ic_notification_placeholder),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .align(Alignment.CenterVertically)
                    .clip(CircleShape)
                    .size(48.dp),
            )

            Column(modifier = Modifier.fillMaxHeight()) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = chatRoom.sender,
                        style = CareTheme.typography.subtitle3,
                        color = CareTheme.colors.black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 15.dp),
                    )

                    Text(
                        text = chatRoom.lastSentAt.formatRelativeDateTime(),
                        style = CareTheme.typography.caption1,
                        color = CareTheme.colors.gray500,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = chatRoom.lastMessage,
                        style = CareTheme.typography.caption1,
                        color = CareTheme.colors.gray300,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 15.dp),
                    )

                    val unReadMessageColor = if (chatRoom.unReadMessageCount > 0) {
                        CareTheme.colors.orange500
                    } else {
                        Color.Transparent
                    }

                    Box(
                        modifier = Modifier
                            .height(22.dp)
                            .wrapContentWidth()
                            .widthIn(min = 22.dp)
                            .clip(RoundedCornerShape(300.dp))
                            .background(unReadMessageColor),
                    ) {
                        Text(
                            text = if (chatRoom.unReadMessageCount != 0)
                                chatRoom.unReadMessageCount.formatUnReadNumber() else "",
                            style = CareTheme.typography.caption1.copy(fontWeight = FontWeight.Bold),
                            color = CareTheme.colors.white000,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .align(Alignment.Center),
                        )
                    }
                }
            }
        }
    }
}
