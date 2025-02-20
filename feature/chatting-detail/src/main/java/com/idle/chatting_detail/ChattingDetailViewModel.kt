package com.idle.chatting_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatMessage
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.usecase.chat.GetChatMessagesUseCase
import com.idle.domain.usecase.chat.SubscribeChatMessageUseCase
import com.idle.domain.usecase.profile.GetCenterProfileUseCase
import com.idle.domain.usecase.profile.GetLocalMyCenterProfileUseCase
import com.idle.domain.usecase.profile.GetLocalMyWorkerProfileUseCase
import com.idle.domain.usecase.profile.GetWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChattingDetailViewModel @Inject constructor(
    private val getLocalMyCenterProfileUseCase: GetLocalMyCenterProfileUseCase,
    private val getLocalMyWorkerProfileUseCase: GetLocalMyWorkerProfileUseCase,
    private val getCenterProfileUseCase: GetCenterProfileUseCase,
    private val getWorkerProfileUseCase: GetWorkerProfileUseCase,
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val subscribeChatMessageUseCase: SubscribeChatMessageUseCase,
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
                    getWorkerProfileUseCase(senderId).onSuccess {
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
                    getCenterProfileUseCase(senderId).onSuccess {
                        _centerProfile.value = it
                    }.onFailure {
                        errorHelper.sendError(it)
                    }
                }

                getLocalMyWorkerProfileUseCase().onSuccess {
                    _workerProfile.value = it
                    Log.d("test", it.toString())
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
        Log.d("test", "호출")
        if (_callType.value == MessageCallType.END) return@launch

        Log.d("test", "호출2")

        getChatMessagesUseCase(
            userType = myUserType,
            roomId = roomId,
            messageId = _chatMessages.value?.first()?.id,
        ).onSuccess { messages ->
            Log.d("test", messages.toString())

            if (messages.size < 50) {
                _callType.value = MessageCallType.END
            }

            _chatMessages.value = messages.plus(_chatMessages.value ?: emptyList())
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun subscribeChatMessage() = viewModelScope.launch {
        subscribeChatMessageUseCase().collect { chatMessage ->
            _chatMessages.value = (_chatMessages.value ?: emptyList()) + chatMessage
        }
    }
}

enum class MessageCallType {
    PAGING, END
}
