package com.idle.center.chatting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.chat.ChatRoom
import com.idle.domain.model.chat.ReadMessage
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.repositorry.ChatRepository
import com.idle.domain.repositorry.ProfileRepository
import com.idle.domain.usecase.profile.GetLocalMyCenterProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterChattingViewModel @Inject constructor(
    private val getLocalMyCenterProfileUseCase: GetLocalMyCenterProfileUseCase,
    private val profileRepository: ProfileRepository,
    private val chatRepository: ChatRepository,
    private val errorHelper: ErrorHelper,
    val navigationHelper: com.idle.navigation.NavigationHelper,
) : ViewModel() {
    private val _myProfile = MutableStateFlow<CenterProfile?>(null)
    val myProfile = _myProfile.asStateFlow()

    private val _chatRoomMap = MutableStateFlow<LinkedHashMap<String, ChatRoom>>(LinkedHashMap())
    val chatRoomList = _chatRoomMap
        .map { it.values.toList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null,
        )

    internal fun subscribeChatMessage() = viewModelScope.launch {
        val myCenterProfile = getLocalMyCenterProfileUseCase().getOrThrow()
        chatRepository.subscribeChatMessage(myCenterProfile.centerId).collect { message ->
            when (message) {
                is ChatMessage -> handleChatMessage(message)
                is ReadMessage -> handleReadMessage(message)
            }
        }
    }

    private suspend fun handleChatMessage(message: ChatMessage) {
        val updatedMap = LinkedHashMap(_chatRoomMap.value) // 기존 맵을 복사
        val roomId = message.roomId
        val chatRoom = updatedMap[roomId]

        if (chatRoom != null) {
            // 기존 방이 있으면 업데이트 후 최상단으로 올리기 위해 제거 후 다시 추가
            updatedMap.remove(roomId)
            updatedMap[roomId] = chatRoom.copy(
                lastMessage = message.content,
                unReadMessageCount = chatRoom.unReadMessageCount + 1,
            )
        } else {
            // 새로운 방이면 새로 생성 후 최상단에 추가
            val opponentProfile = profileRepository.getWorkerProfile(message.senderId)
                .getOrNull() ?: return

            val newChatRoom = ChatRoom(
                id = roomId,
                lastMessage = message.content,
                opponentId = message.senderId,
                opponentName = opponentProfile.workerName,
                lastMessageTime = message.createdAt,
                unReadMessageCount = 1,
                opponentProfileImageUrl = opponentProfile.profileImageUrl,
            )
            updatedMap[roomId] = newChatRoom
        }

        _chatRoomMap.value = updatedMap
    }

    private fun handleReadMessage(message: ReadMessage) {

    }

    internal fun getChatRoomList() = viewModelScope.launch {
        chatRepository.getChatRooms(userType = UserType.CENTER).onSuccess {
            _chatRoomMap.value = LinkedHashMap<String, ChatRoom>().apply {
                it.forEach { chatRoom -> put(chatRoom.id, chatRoom) }
            }
        }.onFailure { errorHelper.sendError(it) }
    }
}
