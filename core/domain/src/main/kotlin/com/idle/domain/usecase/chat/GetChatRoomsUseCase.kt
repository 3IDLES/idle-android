package com.idle.domain.usecase.chat

import com.idle.domain.model.auth.UserType
import com.idle.domain.model.chat.ChatRoom
import com.idle.domain.model.chat.ChatRoomWithOpponentInfo
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.Profile
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.repositorry.ChatRepository
import com.idle.domain.repositorry.ProfileRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetChatRoomsUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(
        userType: UserType,
        userId: String,
    ): List<ChatRoomWithOpponentInfo> = coroutineScope {
        // 로컬 저장소에서 채팅 방 목록 조회
        val chatRooms: List<ChatRoom> = chatRepository.retrieveChatRooms(userId)

        val opponentProfiles = chatRooms.map { chatRoom ->
            async {
                when (userType) {
                    UserType.CENTER ->
                        profileRepository.getWorkerProfile(chatRoom.opponentId)

                    UserType.WORKER ->
                        profileRepository.getCenterProfile(chatRoom.opponentId)
                }
            }
        }.awaitAll()

        // 채팅 방과 상대 프로필을 결합하여 UI용 모델 생성
        chatRooms.zip(opponentProfiles) { chatRoom, profile ->
            mapToRoomWithOpponentInfo(chatRoom, profile)
        }
            .sortedBy { it.lastMessageTime }
    }

    private fun mapToRoomWithOpponentInfo(
        chatRoom: ChatRoom,
        profile: Profile,
    ): ChatRoomWithOpponentInfo {
        val (opponentName, profileUrl) = when (profile) {
            is WorkerProfile -> profile.workerName to profile.profileImageUrl
            is CenterProfile -> profile.centerName to profile.profileImageUrl
        }

        return ChatRoomWithOpponentInfo(
            id = chatRoom.id,
            opponentId = chatRoom.opponentId,
            opponentName = opponentName,
            lastMessage = chatRoom.lastMessage,
            lastMessageTime = chatRoom.lastMessageTime,
            unReadMessageCount = chatRoom.unReadMessageCount,
            opponentProfileImageUrl = profileUrl,
        )
    }
}
