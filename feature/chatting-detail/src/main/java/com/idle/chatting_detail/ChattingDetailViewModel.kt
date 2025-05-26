package com.idle.chatting_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.chat.ReadMessage
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.repositorry.ChatRepository
import com.idle.domain.repositorry.ProfileRepository
import com.idle.domain.usecase.profile.GetMyCenterProfileUseCase
import com.idle.domain.usecase.profile.GetMyWorkerProfileUseCase
import com.idle.navigation.NavigationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChattingDetailViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val chatRepository: ChatRepository,
    private val getMyWorkerProfileUseCase: GetMyWorkerProfileUseCase,
    private val getMyCenterProfileUseCase: GetMyCenterProfileUseCase,
    private val errorHelper: ErrorHelper,
    val navigationHelper: NavigationHelper,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val chatroomId: String = requireNotNull(savedStateHandle["chattingRoomId"]) {
        "chattingRoomId is missing in savedStateHandle"
    }
    private val opponentId: String = requireNotNull(savedStateHandle["opponentId"]) {
        "opponentId is missing in savedStateHandle"
    }
    val myUserType: UserType = UserType.create(
        requireNotNull(savedStateHandle["myUserType"]) { "myUserType is missing" }
    )
    val myId: String = requireNotNull(savedStateHandle["myId"]) {
        "myId is missing in savedStateHandle"
    }
    private var unReadMessageCount: Int = requireNotNull(savedStateHandle["unReadMessageCount"]) {
        "unReadMessageCount is missing in savedStateHandle"
    }
    private val fromJobPosting: Boolean = requireNotNull(savedStateHandle["fromJobPosting"]) {
        "fromJobPosting is missing in savedStateHandle"
    }

    private val _writingText = MutableStateFlow<String>("")
    val writingText = _writingText.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>?>(null)
    val chatMessages = _chatMessages.asStateFlow()

    private val _workerProfile = MutableStateFlow<WorkerProfile?>(null)
    val workerProfile = _workerProfile.asStateFlow()

    private val _centerProfile = MutableStateFlow<CenterProfile?>(null)
    val centerProfile = _centerProfile.asStateFlow()

    private val _callType = MutableStateFlow<MessageCallType>(MessageCallType.PAGING)

    internal fun setWritingText(text: String) {
        _writingText.value = text
    }

    internal suspend fun getUserProfile() = coroutineScope {
        when (myUserType) {
            UserType.CENTER -> {
                launch {
                    profileRepository.getWorkerProfile(opponentId).onSuccess {
                        _workerProfile.value = it
                    }.onFailure {
                        errorHelper.sendError(it)
                    }
                }

                getMyCenterProfileUseCase().onSuccess {
                    _centerProfile.value = it
                }.onFailure {
                    errorHelper.sendError(it)
                }
            }

            UserType.WORKER -> {
                launch {
                    profileRepository.getCenterProfile(opponentId).onSuccess {
                        _centerProfile.value = it
                    }.onFailure {
                        errorHelper.sendError(it)
                    }
                }

                getMyWorkerProfileUseCase().onSuccess {
                    _workerProfile.value = it
                }.onFailure {
                    errorHelper.sendError(it)
                }
            }
        }
    }

    internal suspend fun getChatMessages() {
        if (_callType.value == MessageCallType.END) return

        if (fromJobPosting) {
            chatRepository.retrieveChatRoomMessages(
                roomId = chatroomId,
                myId = myId,
                messageId = _chatMessages.value?.first()?.id,
            ).onSuccess { messages ->
                if (messages.isEmpty()) _callType.value = MessageCallType.END

                _chatMessages.value = messages.plus(_chatMessages.value ?: emptyList())
            }.onFailure {
                errorHelper.sendError(it)
            }
        } else {
            chatRepository.getChatRoomMessages(
                roomId = chatroomId,
                messageId = _chatMessages.value?.first()?.id,
                userType = myUserType,
                myId = myId,
                unReadMessageCount = if (unReadMessageCount >= 0) unReadMessageCount else null,
            ).onSuccess { messages ->
                if (messages.isEmpty()) _callType.value = MessageCallType.END
                unReadMessageCount -= messages.size

                _chatMessages.value = messages.plus(_chatMessages.value ?: emptyList())
            }.onFailure {
                errorHelper.sendError(it)
            }
        }
    }

    internal fun connectWebsocket() = viewModelScope.launch {
        chatRepository.connectWebSocket().onSuccess {
            subscribeChatMessage()
        }
    }

    internal fun disconnectWebsocket() = viewModelScope.launch {
        chatRepository.disconnectWebSocket()
    }

    private suspend fun subscribeChatMessage() {
        chatRepository.subscribeChatMessage(myId, myUserType)
            .catch {
                errorHelper.sendError(it)
            }.collect { message ->
                when (message) {
                    is ChatMessage -> {
                        if (message.senderId == myId) readMessage()
                        readMessage()
                        _chatMessages.value = (_chatMessages.value ?: emptyList()) + message
                    }

                    is ReadMessage -> {
                        _chatMessages.value = _chatMessages.value?.map {
                            if (it.receiverId == message.opponentId) it.copy(isRead = true) else it
                        }
                    }
                }
            }
    }

    internal fun sendMessage() = viewModelScope.launch {
        val senderName = if (myUserType == UserType.CENTER) _centerProfile.value!!.centerName
        else _workerProfile.value!!.workerName

        chatRepository.sendMessage(
            chatroomId = chatroomId,
            myId = myId,
            userType = myUserType,
            receiverId = opponentId,
            senderName = senderName,
            content = _writingText.value,
        ).onSuccess {
            _writingText.value = ""
        }.onFailure { errorHelper.sendError(it) }
    }

    internal suspend fun readMessage() {
        val lastOpponentMessageSequence = _chatMessages.value?.lastOrNull()?.sequence ?: return

        chatRepository.readMessage(
            chatroomId = chatroomId,
            myId = myId,
            opponentId = opponentId,
            userType = myUserType,
            sequence = lastOpponentMessageSequence
        ).onSuccess {
            _chatMessages.value = _chatMessages.value?.map {
                if (it.receiverId == myId) it.copy(isRead = true) else it
            }
        }
    }
}

enum class MessageCallType {
    PAGING, END;
}
