package com.idle.network.source.profile

import com.idle.network.api.UserApi
import com.idle.network.model.auth.GetWorkerIdResponse
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
    private val userApi: UserApi,
) {
    suspend fun getMyCenterProfile(): Result<GetCenterProfileResponse> =
        userApi.getMyCenterProfile().onResponse()

    suspend fun updateMyCenterProfile(updateCenterProfileRequest: UpdateCenterProfileRequest): Result<Unit> =
        userApi.updateMyCenterProfile(updateCenterProfileRequest).onResponse()

    suspend fun getCenterProfile(centerId: String): Result<GetCenterProfileResponse> =
        userApi.getCenterProfile(centerId).onResponse()

    suspend fun getProfileImageUploadUrl(
        userType: String,
        imageFileExtension: String
    ): Result<UploadProfileImageUrlResponse> = userApi.getImageUploadUrl(
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

        return userApi.uploadProfileImage(
            uploadUrl = uploadUrl,
            requestImage = requestImage,
        ).onResponse()
    }

    suspend fun callbackImageUpload(
        userType: String,
        callbackImageUploadRequest: CallbackImageUploadRequest,
    ): Result<Unit> = userApi.callbackImageUpload(
        userType = userType,
        callbackImageUploadRequest = callbackImageUploadRequest,
    ).onResponse()

    suspend fun getMyWorkerProfile(): Result<GetWorkerProfileResponse> =
        userApi.getMyWorkerProfile().onResponse()

    suspend fun getWorkerProfile(workerId: String): Result<GetWorkerProfileResponse> =
        userApi.getWorkerProfile(workerId).onResponse()

    suspend fun updateWorkerProfile(
        updateWorkerProfileRequest: UpdateWorkerProfileRequest
    ): Result<Unit> = userApi.updateWorkerProfile(updateWorkerProfileRequest)
        .onResponse()

    suspend fun registerCenterProfile(
        registerCenterProfileRequest: RegisterCenterProfileRequest
    ): Result<Unit> = userApi.registerCenterProfile(registerCenterProfileRequest).onResponse()

    suspend fun getWorkerId(): Result<GetWorkerIdResponse> = userApi.getWorkerId().onResponse()
}