package com.idle.center.chatting

import androidx.lifecycle.viewModelScope
import com.idle.binding.NavigationHelper
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.chatting.ChatRoom
import com.idle.domain.model.error.ErrorHandlerHelper
import com.idle.domain.usecase.chatting.GetChatRoomListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterChattingViewModel @Inject constructor(
    private val getChatRoomListUseCase: GetChatRoomListUseCase,
    private val errorHandlerHelper: ErrorHandlerHelper,
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