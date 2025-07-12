package com.idle.network.source

import com.idle.network.api.UserApi
import com.idle.network.model.auth.GetWorkerIdResponse
import com.idle.network.model.profile.CallbackImageUploadRequest
import com.idle.network.model.profile.GetCenterProfileResponse
import com.idle.network.model.profile.GetCenterStatusResponse
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
import javax.inject.Singleton

@Singleton
class ProfileDataSource @Inject constructor(
    private val userApi: UserApi,
) {
    suspend fun getMyCenterProfile(): GetCenterProfileResponse =
        userApi.getMyCenterProfile().onResponse()

    suspend fun updateMyCenterProfile(updateCenterProfileRequest: UpdateCenterProfileRequest): Unit =
        userApi.updateMyCenterProfile(updateCenterProfileRequest).onResponse()

    suspend fun getCenterProfile(centerId: String): GetCenterProfileResponse =
        userApi.getCenterProfile(centerId).onResponse()

    suspend fun getProfileImageUploadUrl(
        userType: String,
        imageFileExtension: String
    ): UploadProfileImageUrlResponse =
        userApi.getImageUploadUrl(userType, imageFileExtension).onResponse()

    suspend fun uploadProfileImage(
        uploadUrl: String,
        imageFileExtension: String,
        imageInputStream: InputStream,
    ) {
        val requestImage = imageInputStream.readBytes()
            .toRequestBody(imageFileExtension.toMediaTypeOrNull())

        userApi.uploadProfileImage(uploadUrl = uploadUrl, requestImage = requestImage)
            .onResponse()
    }

    suspend fun callbackImageUpload(
        userType: String,
        callbackImageUploadRequest: CallbackImageUploadRequest,
    ): Unit = userApi.callbackImageUpload(
        userType = userType,
        callbackImageUploadRequest = callbackImageUploadRequest
    ).onResponse()

    suspend fun getMyWorkerProfile(): GetWorkerProfileResponse =
        userApi.getMyWorkerProfile().onResponse()

    suspend fun getWorkerProfile(workerId: String): GetWorkerProfileResponse =
        userApi.getWorkerProfile(workerId).onResponse()

    suspend fun updateWorkerProfile(updateWorkerProfileRequest: UpdateWorkerProfileRequest): Unit =
        userApi.updateWorkerProfile(updateWorkerProfileRequest).onResponse()

    suspend fun registerCenterProfile(registerCenterProfileRequest: RegisterCenterProfileRequest): Unit =
        userApi.registerCenterProfile(registerCenterProfileRequest).onResponse()

    suspend fun getWorkerId(): GetWorkerIdResponse = userApi.getWorkerId().onResponse()

    suspend fun getCenterStatus(): GetCenterStatusResponse = userApi.getCenterStatus().onResponse()
}
