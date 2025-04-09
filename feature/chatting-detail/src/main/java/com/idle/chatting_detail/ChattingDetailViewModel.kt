package com.idle.chatting_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.repositorry.ChatRepository
import com.idle.domain.repositorry.ProfileRepository
import com.idle.domain.usecase.profile.GetLocalMyCenterProfileUseCase
import com.idle.domain.usecase.profile.GetLocalMyWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChattingDetailViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val getLocalMyWorkerProfileUseCase: GetLocalMyWorkerProfileUseCase,
    private val getLocalMyCenterProfileUseCase: GetLocalMyCenterProfileUseCase,
    private val chatRepository: ChatRepository,
    private val errorHelper: ErrorHelper,
    val navigationHelper: com.idle.navigation.NavigationHelper,
) : ViewModel() {
    private val _writingText = MutableStateFlow<String>("")
    val writingText = _writingText.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>?>(null)
    val chatMessages = _chatMessages.asStateFlow()

    private val _workerProfile = MutableStateFlow<WorkerProfile?>(null)
    val workerProfile = _workerProfile.asStateFlow()

    private val _centerProfile = MutableStateFlow<CenterProfile?>(null)
    val centerProfile = _centerProfile.asStateFlow()

    private val _callType = MutableStateFlow<MessageCallType>(MessageCallType.PAGING)
    val callType = _callType.asStateFlow()

    internal fun setWritingText(text: String) {
        _writingText.value = text
    }

    internal fun getUserProfile(
        myUserType: UserType,
        senderId: String,
    ) = viewModelScope.launch {
        when (myUserType) {
            UserType.CENTER -> {
                launch {
                    profileRepository.getWorkerProfile(senderId).onSuccess {
                        _workerProfile.value = it
                    }.onFailure {
                        errorHelper.sendError(it)
                    }
                }

                getLocalMyCenterProfileUseCase().onSuccess {
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

                getLocalMyWorkerProfileUseCase().onSuccess {
                    _workerProfile.value = it
                }.onFailure {
                    errorHelper.sendError(it)
                }
            }

            else -> Unit
        }
    }

    internal fun getChatMessages(
        myUserType: UserType,
        roomId: String,
    ) = viewModelScope.launch {
        if (_callType.value == MessageCallType.END) return@launch

        chatRepository.getChatRoomMessages(
            userType = myUserType,
            roomId = roomId,
            messageId = _chatMessages.value?.first()?.id,
        ).onSuccess { messages ->
            if (messages.size < 50) _callType.value = MessageCallType.END

            _chatMessages.value = messages.plus(_chatMessages.value ?: emptyList())
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun subscribeChatMessage(userId: String) = viewModelScope.launch {
        chatRepository.subscribeChatMessage(userId)
            .catch {
                Log.d("test", it.stackTraceToString())
                errorHelper.sendError(it)
            }.collect { chatMessage ->
                Log.d("test", chatMessage.toString())

                _chatMessages.value = (_chatMessages.value ?: emptyList()) + chatMessage
            }
    }

    internal fun sendMessage(myUserType: UserType, roomId: String) = viewModelScope.launch {
        chatRepository.sendMessage(
            chatroomId = roomId,
            receiverId = if (myUserType == UserType.CENTER) _workerProfile.value!!.workerId
            else _workerProfile.value!!.workerId,
            senderName = if (myUserType == UserType.CENTER) _centerProfile.value!!.centerName
            else _workerProfile.value!!.workerName,
            content = _writingText.value,
        ).onSuccess {
            _chatMessages.value = (_chatMessages.value ?: emptyList()) + ChatMessage(
                id = UUID.randomUUID().toString(),
                roomId = roomId,
                senderId = if (myUserType == UserType.CENTER) _centerProfile.value!!.centerId
                else _workerProfile.value!!.workerId,
                receiverId = if (myUserType == UserType.CENTER) _workerProfile.value!!.workerId
                else _centerProfile.value!!.centerId,
                content = _writingText.value,
                createdAt = LocalDateTime.now(),
                isRead = false,
            )

            _writingText.value = ""
        }.onFailure { errorHelper.sendError(it) }
    }
}

enum class MessageCallType {
    PAGING, END;
}
