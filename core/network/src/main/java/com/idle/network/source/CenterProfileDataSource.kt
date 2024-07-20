package com.idle.network.source

import com.idle.network.api.CareNetworkApi
import com.idle.network.model.profile.CenterProfileRequest
import com.idle.network.model.profile.CenterProfileResponse
import com.idle.network.model.profile.ProfileImageUploadUrlResponse
import com.idle.network.util.onResponse
import javax.inject.Inject

class CenterProfileDataSource @Inject constructor(
    private val careNetworkApi: CareNetworkApi,
) {
    suspend fun getMyCenterProfile(): Result<CenterProfileResponse> =
        careNetworkApi.getMyCenterProfile().onResponse()

    suspend fun updateMyCenterProfile(centerProfileRequest: CenterProfileRequest): Result<Unit> =
        careNetworkApi.updateMyCenterProfile(centerProfileRequest).onResponse()

    suspend fun getProfileImageUploadUrl(
        userType: String,
        imageFileExtension: String
    ): Result<ProfileImageUploadUrlResponse> = careNetworkApi.getImageUploadUrl(
        userType = userType,
        imageFileExtension = imageFileExtension,
    ).onResponse()

    suspend fun uploadProfileImage(
        uploadUrl: String,
    ): Result<Unit> = careNetworkApi.uploadProfileImage(uploadUrl).onResponse()
}