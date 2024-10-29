package com.idle.worker.chatting

import com.idle.binding.NavigationHelper
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.chatting.ChatRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WorkerChattingViewModel @Inject constructor(
    val navigationHelper: NavigationHelper,
) : BaseViewModel() {
    private val _chatRoomList = MutableStateFlow<List<ChatRoom>?>(emptyList())
    val chatRoomList = _chatRoomList.asStateFlow()
}