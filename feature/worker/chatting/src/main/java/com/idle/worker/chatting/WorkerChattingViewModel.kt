package com.idle.worker.chatting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.domain.model.chatting.ChatRoom
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.repositorry.chatting.ChattingRepository
import com.idle.domain.repositorry.profile.ProfileRepository
import com.idle.domain.usecase.chatting.GetChatRoomListUseCase
import com.idle.domain.usecase.profile.GetLocalMyWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerChattingViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val getLocalMyWorkerProfileUseCase: GetLocalMyWorkerProfileUseCase,
    private val getChatRoomListUseCase: GetChatRoomListUseCase,
    private val chattingRepository: ChattingRepository,
    private val errorHelper: ErrorHelper,
    val navigationHelper: com.idle.navigation.NavigationHelper,
) : ViewModel() {
    private var myProfile: WorkerProfile? = null

    private val _chatRoomMap = MutableStateFlow<LinkedHashMap<String, ChatRoom>>(LinkedHashMap())
    val chatRoomList = _chatRoomMap
        .map { it.values.toList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null,
        )

    init {
        viewModelScope.launch {
            getLocalMyWorkerProfileUseCase().onSuccess {
                myProfile = it
            }.onFailure { errorHelper.sendError(it) }
        }
    }

    internal fun subscribeChatMessage() = viewModelScope.launch {
        chattingRepository.subscribeChatMessage().collect { chatMessage ->
            val updatedMap = LinkedHashMap(_chatRoomMap.value) // 기존 맵을 복사
            val roomId = chatMessage.roomId
            val chatRoom = updatedMap[roomId]

            if (chatRoom != null) {
                // 기존 방이 있으면 업데이트 후 최상단으로 올리기 위해 제거 후 다시 추가
                updatedMap.remove(roomId)
                updatedMap[roomId] = chatRoom.copy(
                    lastMessage = chatMessage.printPlainContents(),
                    unReadMessageCount = chatRoom.unReadMessageCount + 1,
                )
            } else {
                // 새로운 방이면 새로 생성 후 최상단에 추가
                val newChatRoom = ChatRoom(
                    id = roomId,
                    lastMessage = chatMessage.printPlainContents(),
                    sender = chatMessage.senderId,
                    receiver = myProfile?.workerId ?: "",
                    createdAt = chatMessage.createdAt,
                    lastSentAt = chatMessage.createdAt,
                    unReadMessageCount = 1,
                    profileImageUrl = profileRepository.getCenterProfile(chatMessage.senderId)
                        .map { it.profileImageUrl }
                        .getOrNull(),
                )
                updatedMap[roomId] = newChatRoom
            }
            _chatRoomMap.value = updatedMap // StateFlow에 갱신된 맵 할당
        }
    }

    internal fun getChatRoomList() = viewModelScope.launch {
        getChatRoomListUseCase().onSuccess {
            _chatRoomMap.value = LinkedHashMap<String, ChatRoom>().apply {
                it.forEach { chatRoom -> put(chatRoom.id, chatRoom) }
            }
        }.onFailure { errorHelper.sendError(it) }
    }
}
