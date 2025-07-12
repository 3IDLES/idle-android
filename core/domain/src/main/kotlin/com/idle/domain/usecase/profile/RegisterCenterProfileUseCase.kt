package com.idle.domain.usecase.profile

import com.idle.domain.model.auth.UserType
import com.idle.domain.repositorry.ProfileRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterCenterProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(
        centerName: String,
        detailedAddress: String,
        introduce: String,
        lotNumberAddress: String,
        officeNumber: String,
        roadNameAddress: String,
        imageFileUri: String?,
    ) = coroutineScope {
        val registerProfileJob = launch {
            profileRepository.registerCenterProfile(
                centerName = centerName,
                detailedAddress = detailedAddress,
                introduce = introduce,
                lotNumberAddress = lotNumberAddress,
                officeNumber = officeNumber,
                roadNameAddress = roadNameAddress
            )
        }

        val updateProfileImageJob = imageFileUri?.let { uri ->
            if (uri.startsWith("content://")) {
                launch {
                    profileRepository.updateProfileImage(
                        userType = UserType.CENTER.apiValue,
                        imageFileUri = uri,
                        reqWidth = 1340,
                        reqHeight = 1016,
                    )
                }
            } else null
        }

        registerProfileJob.join()
        updateProfileImageJob?.join()
    }
}
