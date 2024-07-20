package com.idle.data.repository.profile

import android.util.Log
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.repositorry.profile.ProfileRepository
import com.idle.network.model.profile.CenterProfileRequest
import com.idle.network.source.CenterProfileDataSource
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val centerProfileDataSource: CenterProfileDataSource,
) : ProfileRepository {
    override suspend fun getMyCenterProfile(): Result<CenterProfile> =
        centerProfileDataSource.getMyCenterProfile().mapCatching { it.toVO() }

    override suspend fun updateCenterProfile(
        officeNumber: String,
        introduce: String?,
    ): Result<Unit> = centerProfileDataSource.updateMyCenterProfile(
        CenterProfileRequest(
            officeNumber = officeNumber,
            introduce = introduce,
        )
    )

    override suspend fun updateCenterProfileImage(
        userType: String,
        imageFileExtension: String
    ): Result<Unit> = runCatching {
        val profileImageUploadUrlResponse = centerProfileDataSource.getProfileImageUploadUrl(
            userType = userType,
            imageFileExtension = imageFileExtension
        ).getOrThrow()

        Log.d("test", profileImageUploadUrlResponse.toString())
//        centerProfileDataSource.uploadProfileImage(profileImageUploadUrlResponse.uploadUrl)
//            .getOrThrow()
    }
}