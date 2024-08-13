package com.idle.network.source.profile

import com.idle.network.api.CareApi
import com.idle.network.model.profile.CallbackImageUploadRequest
import com.idle.network.model.profile.GetCenterProfileResponse
import com.idle.network.model.profile.GetWorkerProfileResponse
import com.idle.network.model.profile.RegisterCenterProfileRequest
import com.idle.network.model.profile.UpdateCenterProfileRequest
import com.idle.network.model.profile.UpdateWorkerProfileRequest
import com.idle.network.model.profile.UploadProfileImageUrlResponse
import com.idle.network.util.onResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream
import javax.inject.Inject

class ProfileDataSource @Inject constructor(
    private val careApi: CareApi,
) {
    suspend fun getMyCenterProfile(): Result<GetCenterProfileResponse> =
        careApi.getMyCenterProfile().onResponse()

    suspend fun updateMyCenterProfile(updateCenterProfileRequest: UpdateCenterProfileRequest): Result<Unit> =
        careApi.updateMyCenterProfile(updateCenterProfileRequest).onResponse()

    suspend fun getProfileImageUploadUrl(
        userType: String,
        imageFileExtension: String
    ): Result<UploadProfileImageUrlResponse> = careApi.getImageUploadUrl(
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

        return careApi.uploadProfileImage(
            uploadUrl = uploadUrl,
            requestImage = requestImage,
        ).onResponse()
    }

    suspend fun callbackImageUpload(
        userType: String,
        callbackImageUploadRequest: CallbackImageUploadRequest,
    ): Result<Unit> = careApi.callbackImageUpload(
        userType = userType,
        callbackImageUploadRequest = callbackImageUploadRequest,
    ).onResponse()

    suspend fun getMyWorkerProfile(): Result<GetWorkerProfileResponse> =
        careApi.getMyWorkerProfile().onResponse()

    suspend fun getWorkerProfile(workerId: String): Result<GetWorkerProfileResponse> =
        careApi.getWorkerProfile(workerId).onResponse()

    suspend fun updateWorkerProfile(
        updateWorkerProfileRequest: UpdateWorkerProfileRequest
    ): Result<Unit> = careApi.updateWorkerProfile(updateWorkerProfileRequest)
        .onResponse()

    suspend fun registerCenterProfile(
        registerCenterProfileRequest: RegisterCenterProfileRequest
    ): Result<Unit> =
        careApi.registerCenterProfile(registerCenterProfileRequest).onResponse()
}