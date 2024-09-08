package com.idle.network.source.profile

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
import com.idle.network.util.safeApiCall
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream
import javax.inject.Inject

class ProfileDataSource @Inject constructor(
    private val userApi: UserApi,
) {
    suspend fun getMyCenterProfile(): Result<GetCenterProfileResponse> =
        safeApiCall { userApi.getMyCenterProfile() }

    suspend fun updateMyCenterProfile(updateCenterProfileRequest: UpdateCenterProfileRequest): Result<Unit> =
        safeApiCall { userApi.updateMyCenterProfile(updateCenterProfileRequest) }

    suspend fun getCenterProfile(centerId: String): Result<GetCenterProfileResponse> =
        safeApiCall { userApi.getCenterProfile(centerId) }

    suspend fun getProfileImageUploadUrl(
        userType: String,
        imageFileExtension: String
    ): Result<UploadProfileImageUrlResponse> = safeApiCall {
        userApi.getImageUploadUrl(userType, imageFileExtension)
    }

    suspend fun uploadProfileImage(
        uploadUrl: String,
        imageFileExtension: String,
        imageInputStream: InputStream,
    ): Result<Unit> {
        val requestImage = imageInputStream.readBytes()
            .toRequestBody(imageFileExtension.toMediaTypeOrNull())

        return safeApiCall {
            userApi.uploadProfileImage(uploadUrl = uploadUrl, requestImage = requestImage)
        }
    }

    suspend fun callbackImageUpload(
        userType: String,
        callbackImageUploadRequest: CallbackImageUploadRequest,
    ): Result<Unit> = safeApiCall {
        userApi.callbackImageUpload(
            userType = userType,
            callbackImageUploadRequest = callbackImageUploadRequest
        )
    }

    suspend fun getMyWorkerProfile(): Result<GetWorkerProfileResponse> =
        safeApiCall { userApi.getMyWorkerProfile() }

    suspend fun getWorkerProfile(workerId: String): Result<GetWorkerProfileResponse> =
        safeApiCall { userApi.getWorkerProfile(workerId) }

    suspend fun updateWorkerProfile(updateWorkerProfileRequest: UpdateWorkerProfileRequest): Result<Unit> =
        safeApiCall { userApi.updateWorkerProfile(updateWorkerProfileRequest) }

    suspend fun registerCenterProfile(registerCenterProfileRequest: RegisterCenterProfileRequest): Result<Unit> =
        safeApiCall { userApi.registerCenterProfile(registerCenterProfileRequest) }

    suspend fun getWorkerId(): Result<GetWorkerIdResponse> =
        safeApiCall { userApi.getWorkerId() }

    suspend fun getCenterStatus(): Result<GetCenterStatusResponse> =
        safeApiCall { userApi.getCenterStatus() }
}
