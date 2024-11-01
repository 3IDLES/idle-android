package com.idle.worker.chatting

import androidx.lifecycle.viewModelScope
import com.idle.binding.NavigationHelper
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.chatting.ChatRoom
import com.idle.domain.model.error.ErrorHandler
import com.idle.domain.usecase.chatting.GetChatRoomListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerChattingViewModel @Inject constructor(
    private val getChatRoomListUseCase: GetChatRoomListUseCase,
    private val errorHandlerHelper: ErrorHandler,
    val navigationHelper: NavigationHelper,
) : BaseViewModel() {
    private val _chatRoomList = MutableStateFlow<List<ChatRoom>?>(emptyList())
    val chatRoomList = _chatRoomList.asStateFlow()

    internal fun getChatRoomList() = viewModelScope.launch {
        getChatRoomListUseCase().onSuccess {
            _chatRoomList.value = it
        }.onFailure { errorHandlerHelper.sendError(it) }
    }
}