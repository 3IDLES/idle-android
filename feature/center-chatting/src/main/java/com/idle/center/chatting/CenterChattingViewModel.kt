package com.idle.center.chatting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.chat.ChatRoomWithOpponentInfo
import com.idle.domain.model.chat.ReadMessage
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.repositorry.ChatRepository
import com.idle.domain.repositorry.ProfileRepository
import com.idle.domain.usecase.chat.GetChatRoomsUseCase
import com.idle.domain.usecase.profile.GetMyCenterProfileUseCase
import com.idle.navigation.NavigationHelper
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
    private val getMyCenterProfileUseCase: GetMyCenterProfileUseCase,
    private val getChatRoomsUseCase: GetChatRoomsUseCase,
    private val profileRepository: ProfileRepository,
    private val chatRepository: ChatRepository,
    private val errorHelper: ErrorHelper,
    val navigationHelper: NavigationHelper,
) : ViewModel() {
    private val _myProfile = MutableStateFlow<CenterProfile?>(null)
    val myProfile = _myProfile.asStateFlow()

    private val _chatRoomMap =
        MutableStateFlow<LinkedHashMap<String, ChatRoomWithOpponentInfo>>(LinkedHashMap())
    val chatRoomList = _chatRoomMap
        .map { it.values.toList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null,
        )

    internal suspend fun initCenterChatting() {
        getMyCenterProfileUseCase().onSuccess {
            _myProfile.value = it
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun subscribeChatMessage() = viewModelScope.launch {
        chatRepository.subscribeChatMessage(_myProfile.value?.centerId ?: return@launch)
            .collect { message ->
                when (message) {
                    is ChatMessage -> handleChatMessage(message)
                    is ReadMessage -> Unit
                }
            }
    }

    private suspend fun handleChatMessage(message: ChatMessage) {
        val updatedMap = LinkedHashMap(_chatRoomMap.value)
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

            val newChatRoom = ChatRoomWithOpponentInfo(
                id = roomId,
                lastMessage = message.content,
                opponentId = message.senderId,
                lastMessageTime = message.createdAt,
                opponentName = opponentProfile.workerName,
                opponentProfileImageUrl = opponentProfile.profileImageUrl,
                unReadMessageCount = 1,
            )
            updatedMap[roomId] = newChatRoom
        }

        _chatRoomMap.value = updatedMap
    }

    internal suspend fun retrieveChatRoomList() {
        getChatRoomsUseCase(
            userType = UserType.CENTER,
            userId = _myProfile.value?.centerId ?: return
        ).onSuccess {
            val newMap = LinkedHashMap<String, ChatRoomWithOpponentInfo>(_chatRoomMap.value)
            it.forEach { chatRoom -> newMap[chatRoom.id] = chatRoom }
            _chatRoomMap.value = newMap
        }.onFailure { errorHelper.sendError(it) }
    }

    internal suspend fun loadChatRoomList() {
        chatRepository.loadChatRooms(
            userId = _myProfile.value?.centerId ?: return,
            userType = UserType.CENTER
        ).onSuccess {
            val newMap = LinkedHashMap<String, ChatRoomWithOpponentInfo>(_chatRoomMap.value)
            it.forEach { chatRoom -> newMap[chatRoom.id] = chatRoom }
            _chatRoomMap.value = newMap
        }
    }
}
