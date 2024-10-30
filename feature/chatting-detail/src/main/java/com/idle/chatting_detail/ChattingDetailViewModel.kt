package com.idle.chatting_detail

import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.chatting.ChatMessage
import com.idle.domain.usecase.profile.GetCenterProfileUseCase
import com.idle.domain.usecase.profile.GetLocalMyCenterProfileUseCase
import com.idle.domain.usecase.profile.GetLocalMyWorkerProfileUseCase
import com.idle.domain.usecase.profile.GetWorkerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChattingDetailViewModel @Inject constructor(
    private val getLocalMyCenterProfileUseCase: GetLocalMyCenterProfileUseCase,
    private val getLocalMyWorkerProfileUseCase: GetLocalMyWorkerProfileUseCase,
    private val getCenterProfileUseCase: GetCenterProfileUseCase,
    private val getWorkerProfileUseCase: GetWorkerProfileUseCase,
) : BaseViewModel() {
    private val _writingText = MutableStateFlow<String>("")
    val writingText = _writingText.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>?>(emptyList())
    val chatMessages = _chatMessages.asStateFlow()

    internal fun setWritingText(text: String) {
        _writingText.value = text
    }
}