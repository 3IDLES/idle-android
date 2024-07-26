package com.idle.network.source.profile

import com.idle.network.api.CareNetworkApi
import com.idle.network.model.profile.CallbackImageUploadRequest
import com.idle.network.model.profile.GetCenterProfileResponse
import com.idle.network.model.profile.GetWorkerProfileResponse
import com.idle.network.model.profile.UpdateCenterProfileRequest
import com.idle.network.model.profile.UpdateWorkerProfileRequest
import com.idle.network.model.profile.UploadProfileImageUrlResponse
import com.idle.network.util.onResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream
import javax.inject.Inject

class ProfileDataSource @Inject constructor(
    private val careNetworkApi: CareNetworkApi,
) {
    suspend fun getMyCenterProfile(): Result<GetCenterProfileResponse> =
        careNetworkApi.getMyCenterProfile().onResponse()

    suspend fun updateMyCenterProfile(updateCenterProfileRequest: UpdateCenterProfileRequest): Result<Unit> =
        careNetworkApi.updateMyCenterProfile(updateCenterProfileRequest).onResponse()

    suspend fun getProfileImageUploadUrl(
        userType: String,
        imageFileExtension: String
    ): Result<UploadProfileImageUrlResponse> = careNetworkApi.getImageUploadUrl(
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

    suspend fun getWorkerProfile(): Result<GetWorkerProfileResponse> =
        careNetworkApi.getWorkerProfile().onResponse()

    suspend fun updateWorkerProfile(
        updateWorkerProfileRequest: UpdateWorkerProfileRequest
    ): Result<Unit> = careNetworkApi.updateWorkerProfile(updateWorkerProfileRequest)
        .onResponse()
}