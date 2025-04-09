package com.idle.domain.usecase.profile

import com.idle.domain.model.auth.UserType
import com.idle.domain.repositorry.ProfileRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpdateCenterProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(
        officeNumber: String,
        introduce: String?,
        imageFileUri: String?,
    ): Result<Unit> = runCatching {
        coroutineScope {
            val updateProfileJob = launch {
                profileRepository.updateCenterProfile(
                    officeNumber = officeNumber,
                    introduce = introduce,
                ).getOrThrow()
            }

            val updateProfileImageJob = imageFileUri?.let { uri ->
                if (uri.startsWith("content://")) {
                    launch {
                        profileRepository.updateProfileImage(
                            userType = UserType.CENTER.apiValue,
                            imageFileUri = uri,
                            reqWidth = 1340,
                            reqHeight = 1016,
                        ).getOrThrow()
                    }
                } else null
            }

            updateProfileJob.join()
            updateProfileImageJob?.join()
        }
    }
}
