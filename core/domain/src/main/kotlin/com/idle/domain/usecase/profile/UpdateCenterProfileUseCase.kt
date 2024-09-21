package com.idle.domain.usecase.profile

import com.idle.domain.model.auth.UserType
import com.idle.domain.repositorry.profile.ProfileRepository
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
            val updateProfileJon = launch {
                profileRepository.updateCenterProfile(
                    officeNumber = officeNumber,
                    introduce = introduce,
                ).getOrThrow()
            }

            val updateProfileImageJob = imageFileUri?.let {
                launch {
                    profileRepository.updateProfileImage(
                        userType = UserType.CENTER.apiValue,
                        imageFileUri = imageFileUri,
                        reqWidth = 1340,
                        reqHeight = 1016,
                    ).getOrThrow()
                }
            }

            updateProfileJon.join()
            updateProfileImageJob?.join()
        }
    }
}