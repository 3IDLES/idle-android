package com.idle.worker.chatting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.common.suspendRunCatching
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.chat.ChatRoomWithOpponentInfo
import com.idle.domain.model.chat.ReadMessage
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.repositorry.ChatRepository
import com.idle.domain.repositorry.ProfileRepository
import com.idle.domain.usecase.chat.GetChatRoomsUseCase
import com.idle.domain.usecase.profile.GetMyWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerChattingViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val getChatRoomsUseCase: GetChatRoomsUseCase,
    private val getMyWorkerProfileUseCase: GetMyWorkerProfileUseCase,
    private val chatRepository: ChatRepository,
    private val errorHelper: ErrorHelper,
    val navigationHelper: com.idle.navigation.NavigationHelper,
) : ViewModel() {
    private val _myProfile = MutableStateFlow<WorkerProfile?>(null)
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

    internal suspend fun initWorkerChatting() {
        suspendRunCatching {
            getMyWorkerProfileUseCase()
        }.onSuccess { profile ->
            _myProfile.value = profile
        }.onFailure { errorHelper.sendError(it) }
    }

    internal suspend fun retrieveChatRoomList() {
        val workerId = _myProfile.value?.workerId ?: return

        suspendRunCatching {
            getChatRoomsUseCase(
                userType = UserType.WORKER,
                userId = workerId,
            )
        }.onSuccess {
            val newMap = LinkedHashMap<String, ChatRoomWithOpponentInfo>(_chatRoomMap.value)
            it.forEach { chatRoom -> newMap[chatRoom.id] = chatRoom }
            _chatRoomMap.value = newMap
        }.onFailure { errorHelper.sendError(it) }
    }

    internal suspend fun loadChatRoomList() {
        val workerId = _myProfile.value?.workerId ?: return

        suspendRunCatching {
            chatRepository.loadChatRooms(
                userId = workerId,
                userType = UserType.WORKER,
            )
        }.onSuccess {
            val newMap = LinkedHashMap<String, ChatRoomWithOpponentInfo>(_chatRoomMap.value)
            it.forEach { chatRoom -> newMap[chatRoom.id] = chatRoom }
            _chatRoomMap.value = newMap
        }
    }

    internal fun connectWebsocket() = viewModelScope.launch {
        suspendRunCatching {
            chatRepository.connectWebSocket()
        }.onSuccess {
            subscribeChatMessage()
        }
    }

    internal fun disconnectWebsocket() = viewModelScope.launch {
        chatRepository.disconnectWebSocket()
    }

    private suspend fun subscribeChatMessage() {
        chatRepository.subscribeChatMessage(
            userId = _myProfile.value?.workerId ?: return,
            userType = UserType.WORKER,
        ).collect { message ->
            when (message) {
                is ChatMessage -> handleNewChat(message)
                is ReadMessage -> Unit
            }
        }
    }

    private suspend fun handleNewChat(chatMessage: ChatMessage) {
        val updatedMap = LinkedHashMap(_chatRoomMap.value)
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
            val opponentProfile = suspendRunCatching {
                profileRepository.getCenterProfile(chatMessage.senderId)
            }.getOrNull() ?: return

            val newChatRoom = ChatRoomWithOpponentInfo(
                id = roomId,
                lastMessage = chatMessage.content,
                opponentId = chatMessage.senderId,
                lastMessageTime = chatMessage.createdAt,
                opponentName = opponentProfile.centerName,
                opponentProfileImageUrl = opponentProfile.profileImageUrl,
                unReadMessageCount = 1,
            )
            updatedMap[roomId] = newChatRoom
        }

        _chatRoomMap.value = updatedMap
    }
}
