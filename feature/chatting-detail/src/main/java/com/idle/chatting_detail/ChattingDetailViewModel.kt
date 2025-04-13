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
    private val chattingRoomId: String = requireNotNull(savedStateHandle["chattingRoomId"]) {
        "chattingRoomId is missing in savedStateHandle"
    }
    private val senderId: String = requireNotNull(savedStateHandle["senderId"]) {
        "senderId is missing in savedStateHandle"
    }
    val receiverUserType: UserType = UserType.create(
        requireNotNull(savedStateHandle["receiverUserType"]) { "receiverUserType is missing" }
    )
    val receiverId: String = requireNotNull(savedStateHandle["receiverId"]) {
        "receiverId is missing in savedStateHandle"
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
        when (receiverUserType) {
            UserType.CENTER -> {
                launch {
                    profileRepository.getWorkerProfile(senderId).onSuccess {
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
                    profileRepository.getCenterProfile(senderId).onSuccess {
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

            else -> Unit
        }
    }

    internal fun getChatMessages() = viewModelScope.launch {
        if (_callType.value == MessageCallType.END) return@launch

        chatRepository.getChatRoomMessages(
            roomId = chattingRoomId,
            messageId = _chatMessages.value?.first()?.id,
            userType = receiverUserType,
        ).onSuccess { messages ->
            if (messages.isEmpty()) _callType.value = MessageCallType.END

            _chatMessages.value = messages.plus(_chatMessages.value ?: emptyList())
        }.onFailure { errorHelper.sendError(it) }
    }

    internal suspend fun subscribeChatMessage() {
        chatRepository.subscribeChatMessage(receiverId)
            .catch {
                errorHelper.sendError(it)
            }.collect { message ->
                when (message) {
                    is ChatMessage -> {
                        if (message.senderId != receiverId) readMessage()

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
        val receiverId = if (receiverUserType == UserType.CENTER) _workerProfile.value!!.workerId
        else _centerProfile.value!!.centerId

        val senderName = if (receiverUserType == UserType.CENTER) _centerProfile.value!!.centerName
        else _workerProfile.value!!.workerName

        chatRepository.sendMessage(
            chatroomId = chattingRoomId,
            receiverId = receiverId,
            senderName = senderName,
            content = _writingText.value,
        ).onSuccess {
            _writingText.value = ""
        }.onFailure {
            errorHelper.sendError(it)
        }
    }

    internal suspend fun readMessage() {
        val opponentId = if (receiverUserType == UserType.CENTER) _workerProfile.value!!.workerId
        else _centerProfile.value!!.centerId

        val myId = if (receiverUserType == UserType.CENTER) _centerProfile.value!!.centerId
        else _workerProfile.value!!.workerId

        chatRepository.readMessage(
            chatroomId = chattingRoomId,
            opponentId = opponentId,
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
