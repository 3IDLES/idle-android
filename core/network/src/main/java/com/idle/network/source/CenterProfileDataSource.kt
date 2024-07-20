package com.idle.network.source

import com.idle.domain.model.profile.ImageFileInfo
import com.idle.network.api.CareNetworkApi
import com.idle.network.model.profile.CallbackImageUploadRequest
import com.idle.network.model.profile.CenterProfileRequest
import com.idle.network.model.profile.CenterProfileResponse
import com.idle.network.model.profile.ProfileImageUploadUrlResponse
import com.idle.network.util.onResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
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
        imageFileInfo: ImageFileInfo,
    ): Result<Unit> {
        val requestImage = imageFileInfo.run {
            imageInputStream.readBytes()
                .toRequestBody(imageFileExtension.value.toMediaTypeOrNull())
        }

        return careNetworkApi.uploadProfileImage(
            uploadUrl = uploadUrl,
            requestImage = requestImage,
        ).onResponse()
    }

    suspend fun callbackImageUpload(
        userType: String,
        callbackImageUploadRequest: CallbackImageUploadRequest,
    ): Result<Unit> = careNetworkApi.callbackImageUpload(
        userType = userType,
        callbackImageUploadRequest = callbackImageUploadRequest,
    ).onResponse()
}