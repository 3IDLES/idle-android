package com.idle.domain.usecase.chat

import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatRoom
import com.idle.domain.repositorry.chatting.ChatRepository
import javax.inject.Inject

class GetChatRoomListUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(userType: UserType): Result<List<ChatRoom>> =
        chatRepository.getChatRooms(userType)
}
