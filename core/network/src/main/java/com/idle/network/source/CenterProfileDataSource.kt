package com.idle.network.source

import com.idle.network.api.CareNetworkApi
import com.idle.network.model.profile.CallbackImageUploadRequest
import com.idle.network.model.profile.CenterProfileRequest
import com.idle.network.model.profile.CenterProfileResponse
import com.idle.network.model.profile.ProfileImageUploadUrlResponse
import com.idle.network.util.onResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream
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
        imageFileExtension: String,
        imageInputStream: InputStream,
    ): Result<Unit> {
        val requestImage = imageInputStream.readBytes()
            .toRequestBody(imageFileExtension.toMediaTypeOrNull())

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