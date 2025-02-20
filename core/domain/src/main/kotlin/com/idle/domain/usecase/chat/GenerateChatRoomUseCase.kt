package com.idle.domain.usecase.chat

import com.idle.domain.model.auth.UserType
import com.idle.domain.repositorry.chatting.ChatRepository
import javax.inject.Inject

class GenerateChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(userType: UserType, opponentId: String): Result<String> =
        chatRepository.generateChatRooms(
            userType = userType,
            opponentId = opponentId,
        )
}
