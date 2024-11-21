package com.idle.chatting_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chatting.ChatMessage
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.usecase.chatting.GetChatMessagesUseCase
import com.idle.domain.usecase.chatting.SubscribeChatMessageUseCase
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

    internal fun setWritingText(text: String) {
        _writingText.value = text
    }

    internal fun getUserProfile(
        receiverUserType: UserType,
        senderId: String,
    ) = viewModelScope.launch {
        when (receiverUserType) {
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
                }.onFailure {
                    errorHelper.sendError(it)
                }
            }
        }
    }

    internal fun getChatMessages(roomId: String) = viewModelScope.launch {
        getChatMessagesUseCase(roomId).onSuccess {
            _chatMessages.value = it
        }
    }

    internal fun subscribeChatMessage() = viewModelScope.launch {
        subscribeChatMessageUseCase().collect { chatMessage ->
            _chatMessages.value = (_chatMessages.value ?: emptyList()) + chatMessage
        }
    }
}