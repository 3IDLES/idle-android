package com.idle.domain.usecase.profile

import com.idle.domain.model.auth.UserRole
import com.idle.domain.repositorry.profile.ProfileRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
            val updateProfileDeferred = async {
                profileRepository.updateCenterProfile(
                    officeNumber = officeNumber,
                    introduce = introduce,
                ).getOrThrow()
            }

            val updateProfileImageDeferred = imageFileUri?.let {
                async {
                    profileRepository.updateProfileImage(
                        userType = UserRole.CENTER.apiValue,
                        imageFileUri = imageFileUri,
                    ).getOrThrow()
                }
            }

            updateProfileDeferred.await()
            updateProfileImageDeferred?.await()
        }
    }
}