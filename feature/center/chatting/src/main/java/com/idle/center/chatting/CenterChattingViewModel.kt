package com.idle.center.chatting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatRoom
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.usecase.chatting.GetChatRoomListUseCase
import com.idle.domain.usecase.chatting.SubscribeChatMessageUseCase
import com.idle.domain.usecase.profile.GetWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterChattingViewModel @Inject constructor(
    private val getWorkerProfileUseCase: GetWorkerProfileUseCase,
    private val getChatRoomListUseCase: GetChatRoomListUseCase,
    private val subscribeChatMessageUseCase: SubscribeChatMessageUseCase,
    private val errorHelper: ErrorHelper,
    val navigationHelper: com.idle.navigation.NavigationHelper,
) : ViewModel() {
    private val _chatRoomMap = MutableStateFlow<LinkedHashMap<String, ChatRoom>>(LinkedHashMap())
    val chatRoomList = _chatRoomMap
        .map { it.values.toList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null,
        )

    internal fun subscribeChatMessage() = viewModelScope.launch {
        subscribeChatMessageUseCase().collect { chatMessage ->
            val updatedMap = LinkedHashMap(_chatRoomMap.value) // 기존 맵을 복사
            val roomId = chatMessage.roomId
            val chatRoom = updatedMap[roomId]

            if (chatRoom != null) {
                // 기존 방이 있으면 업데이트 후 최상단으로 올리기 위해 제거 후 다시 추가
                updatedMap.remove(roomId)
                updatedMap[roomId] = chatRoom.copy(
                    lastMessage = chatMessage.content,
                    unReadMessageCount = chatRoom.unReadMessageCount + 1,
                )
            } else {
                // 새로운 방이면 새로 생성 후 최상단에 추가
                val opponentProfile = getWorkerProfileUseCase(chatMessage.senderId)
                    .getOrNull() ?: return@collect

                val newChatRoom = ChatRoom(
                    id = roomId,
                    lastMessage = chatMessage.content,
                    myId = chatMessage.senderId,
                    opponentId = chatMessage.senderId,
                    opponentName = opponentProfile.workerName,
                    lastMessageTime = chatMessage.createdAt,
                    unReadMessageCount = 1,
                    opponentProfileImageUrl = opponentProfile.profileImageUrl,
                )
                updatedMap[roomId] = newChatRoom
            }

            _chatRoomMap.value = updatedMap
        }
    }

    internal fun getChatRoomList() = viewModelScope.launch {
        getChatRoomListUseCase(userType = UserType.CENTER).onSuccess {
            _chatRoomMap.value = LinkedHashMap<String, ChatRoom>().apply {
                it.forEach { chatRoom -> put(chatRoom.id, chatRoom) }
            }
        }.onFailure { errorHelper.sendError(it) }
    }
}
