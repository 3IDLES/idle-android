package com.idle.center.chatting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.common.suspendRunCatching
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.chat.ChatRoomWithOpponentInfo
import com.idle.domain.model.chat.ReadMessage
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.repositorry.ChatRepository
import com.idle.domain.repositorry.ProfileRepository
import com.idle.domain.usecase.chat.GetChatRoomsUseCase
import com.idle.navigation.NavigationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterChattingViewModel @Inject constructor(
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

    private val _chatRoomList = MutableStateFlow<List<ChatRoomWithOpponentInfo>?>(null)
    val chatRoomList = _chatRoomList.asStateFlow()

    internal suspend fun initCenterChatting() {
        suspendRunCatching {
            profileRepository.getMyCenterProfile()
        }.onSuccess {
            _myProfile.value = it
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun connectWebsocket() = viewModelScope.launch {
        suspendRunCatching {
            chatRepository.connectWebSocket()
        }.onSuccess {
            subscribeChatMessage()
        }
    }

    internal fun disconnectWebsocket() = viewModelScope.launch {
        suspendRunCatching {
            chatRepository.disconnectWebSocket()
        }
    }

    private fun subscribeChatMessage() = viewModelScope.launch {
        chatRepository.subscribeChatMessage(
            userId = _myProfile.value?.centerId ?: return@launch,
            userType = UserType.CENTER,
        ).catch {
            errorHelper.sendError(it)
        }.collect { message ->
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
            val opponentProfile = suspendRunCatching {
                profileRepository.getWorkerProfile(message.senderId)
            }.getOrNull() ?: return

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
        _chatRoomList.value = _chatRoomMap.value.values.toList()
    }

    internal suspend fun retrieveChatRoomList() {
        val centerId = _myProfile.value?.centerId ?: return

        suspendRunCatching {
            getChatRoomsUseCase(
                userType = UserType.CENTER,
                userId = centerId,
            )
        }.onSuccess {
            val newMap = LinkedHashMap<String, ChatRoomWithOpponentInfo>().apply {
                putAll(_chatRoomMap.value)
                it.forEach { chatRoom -> this[chatRoom.id] = chatRoom }
            }
            _chatRoomMap.value = newMap
            _chatRoomList.value = _chatRoomMap.value.values.toList()
        }.onFailure { errorHelper.sendError(it) }
    }

    internal suspend fun loadChatRoomList() {
        val centerId = _myProfile.value?.centerId ?: return

        suspendRunCatching {
            chatRepository.loadChatRooms(
                userId = centerId,
                userType = UserType.CENTER
            )
        }.onSuccess { response ->
            val newMap = LinkedHashMap<String, ChatRoomWithOpponentInfo>().apply {
                putAll(_chatRoomMap.value)
                response.forEach { chatRoom -> this[chatRoom.id] = chatRoom }
            }
            _chatRoomMap.value = newMap
            _chatRoomList.value = _chatRoomMap.value.values.toList()
        }
    }
}
