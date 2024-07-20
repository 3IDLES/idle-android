package com.idle.domain.usecase.profile

import com.idle.domain.model.auth.UserRole
import com.idle.domain.model.profile.ImageFileInfo
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
        profileImageUri: ImageFileInfo?,
    ): Result<Unit> = coroutineScope {
        val updateProfileDeferred = async {
            profileRepository.updateCenterProfile(
                officeNumber = officeNumber,
                introduce = introduce,
            )
        }

        profileImageUri?.let {
            async {
                profileRepository.updateCenterProfileImage(
                    userType = UserRole.CENTER.apiValue,
                    imageFileExtension = profileImageUri.imageFileExtension.name,
                )
            }.await()
        }

        updateProfileDeferred.await()
    }
}