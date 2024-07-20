package com.idle.data.repository.profile

import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.ImageFileInfo
import com.idle.domain.repositorry.profile.ProfileRepository
import com.idle.network.model.profile.CallbackImageUploadRequest
import com.idle.network.model.profile.CenterProfileRequest
import com.idle.network.model.profile.ProfileImageUploadUrlResponse
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

    override suspend fun updateProfileImage(
        userType: String,
        imageFileInfo: ImageFileInfo,
    ): Result<Unit> = runCatching {
        val profileImageUploadUrlResponse =
            getProfileImageUploadUrl(userType, imageFileInfo.imageFileExtension.name).getOrThrow()

        uploadProfileImage(
            uploadUrl = profileImageUploadUrlResponse.uploadUrl,
            imageFileInfo = imageFileInfo
        ).getOrThrow()

        callbackImageUpload(
            userType = userType,
            imageId = profileImageUploadUrlResponse.imageId,
            imageFileExtension = profileImageUploadUrlResponse.imageFileExtension
        )
    }

    private suspend fun getProfileImageUploadUrl(
        userType: String,
        imageFileExtension: String
    ): Result<ProfileImageUploadUrlResponse> =
        centerProfileDataSource.getProfileImageUploadUrl(userType, imageFileExtension)

    private suspend fun uploadProfileImage(
        uploadUrl: String,
        imageFileInfo: ImageFileInfo
    ): Result<Unit> = centerProfileDataSource.uploadProfileImage(uploadUrl, imageFileInfo)

    private suspend fun callbackImageUpload(
        userType: String,
        imageId: String,
        imageFileExtension: String
    ): Result<Unit> = centerProfileDataSource.callbackImageUpload(
        userType = userType,
        callbackImageUploadRequest = CallbackImageUploadRequest(
            imageId = imageId,
            imageFileExtension = imageFileExtension,
        )
    )
}