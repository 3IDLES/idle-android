package com.idle.data.repository.profile

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.idle.datastore.datasource.UserInfoDataSource
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.JobSearchStatus
import com.idle.domain.model.profile.MIMEType
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.repositorry.profile.ProfileRepository
import com.idle.network.model.profile.CallbackImageUploadRequest
import com.idle.network.model.profile.RegisterCenterProfileRequest
import com.idle.network.model.profile.UpdateCenterProfileRequest
import com.idle.network.model.profile.UpdateWorkerProfileRequest
import com.idle.network.model.profile.UploadProfileImageUrlResponse
import com.idle.network.source.profile.ProfileDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.io.InputStream
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource,
    private val userInfoDataSource: UserInfoDataSource,
    @ApplicationContext private val context: Context,
) : ProfileRepository {
    override suspend fun getMyUserRole() = userInfoDataSource.userRole.first()

    override suspend fun getMyCenterProfile(): Result<CenterProfile> =
        profileDataSource.getMyCenterProfile().mapCatching { it.toVO() }
            .onSuccess {
                userInfoDataSource.setUserInfo(it.toString())
                Result.success(it)
            }

    override suspend fun getLocalMyCenterProfile(): Result<CenterProfile> = runCatching {
        val userInfoString = userInfoDataSource.userInfo.first()
            .takeIf { it.isNotBlank() } ?: throw IllegalArgumentException("Missing UserInfo")

        val properties = userInfoString.removePrefix("CenterProfile(").removeSuffix(")")
            .split(", ")
            .associate {
                val (key, value) = it.split("=")
                key to value
            }

        CenterProfile(
            centerName = properties["centerName"]
                ?: throw IllegalArgumentException("Missing centerName"),
            officeNumber = properties["officeNumber"]
                ?: throw IllegalArgumentException("Missing officeNumber"),
            roadNameAddress = properties["roadNameAddress"]
                ?: throw IllegalArgumentException("Missing roadNameAddress"),
            lotNumberAddress = properties["lotNumberAddress"]
                ?: throw IllegalArgumentException("Missing lotNumberAddress"),
            detailedAddress = properties["detailedAddress"]
                ?: throw IllegalArgumentException("Missing detailedAddress"),
            longitude = properties["longitude"]?.toDoubleOrNull()
                ?: throw NumberFormatException("Invalid longitude format"),
            latitude = properties["latitude"]?.toDoubleOrNull()
                ?: throw NumberFormatException("Invalid latitude format"),
            introduce = properties["introduce"],
            profileImageUrl = properties["profileImageUrl"]
                ?: throw IllegalArgumentException("Missing profileImageUrl")
        )
    }

    override suspend fun getMyWorkerProfile(): Result<WorkerProfile> =
        profileDataSource.getMyWorkerProfile().mapCatching { it.toVo() }
            .onSuccess {
                userInfoDataSource.setUserInfo(it.toString())
                Result.success(it)
            }

    override suspend fun getLocalMyWorkerProfile(): Result<WorkerProfile> = runCatching {
        val userInfoString = userInfoDataSource.userInfo.first()
            .takeIf { it.isNotBlank() } ?: throw IllegalArgumentException("Missing UserInfo")

        val properties = userInfoString.removePrefix("WorkerProfile(").removeSuffix(")")
            .split(", ")
            .associate {
                val (key, value) = it.split("=")
                key to value
            }

        WorkerProfile(
            workerName = properties["workerName"]
                ?: throw IllegalArgumentException("Missing workerName"),
            age = properties["age"]?.toInt() ?: throw NumberFormatException("Invalid age format"),
            gender = Gender.create(properties["gender"]),
            experienceYear = properties["experienceYear"]?.toIntOrNull(),
            phoneNumber = properties["phoneNumber"]
                ?: throw IllegalArgumentException("Missing phoneNumber"),
            roadNameAddress = properties["roadNameAddress"]
                ?: throw IllegalArgumentException("Missing roadNameAddress"),
            lotNumberAddress = properties["lotNumberAddress"]
                ?: throw IllegalArgumentException("Missing lotNumberAddress"),
            longitude = properties["longitude"]
                ?: throw IllegalArgumentException("Missing longitude"),
            latitude = properties["latitude"] ?: throw IllegalArgumentException("Missing latitude"),
            jobSearchStatus = JobSearchStatus.create(properties["jobSearchStatus"]),
            introduce = properties["introduce"],
            speciality = properties["speciality"],
            profileImageUrl = properties["profileImageUrl"]
        )
    }

    override suspend fun getWorkerProfile(workerId: String): Result<WorkerProfile> =
        profileDataSource.getWorkerProfile(workerId).mapCatching { it.toVo() }

    override suspend fun updateCenterProfile(
        officeNumber: String,
        introduce: String?,
    ): Result<Unit> = profileDataSource.updateMyCenterProfile(
        UpdateCenterProfileRequest(
            officeNumber = officeNumber,
            introduce = introduce,
        )
    )

    override suspend fun updateWorkerProfile(
        experienceYear: Int?,
        roadNameAddress: String,
        lotNumberAddress: String,
        jobSearchStatus: JobSearchStatus,
        introduce: String?,
        speciality: String
    ): Result<Unit> = profileDataSource.updateWorkerProfile(
        UpdateWorkerProfileRequest(
            experienceYear = experienceYear,
            roadNameAddress = roadNameAddress,
            lotNumberAddress = lotNumberAddress,
            jobSearchStatus = jobSearchStatus.name,
            introduce = introduce,
            speciality = speciality
        )
    )

    override suspend fun registerCenterProfile(
        centerName: String,
        detailedAddress: String,
        introduce: String,
        lotNumberAddress: String,
        officeNumber: String,
        roadNameAddress: String
    ): Result<Unit> = profileDataSource.registerCenterProfile(
        RegisterCenterProfileRequest(
            centerName = centerName,
            detailedAddress = detailedAddress,
            introduce = introduce,
            lotNumberAddress = lotNumberAddress,
            officeNumber = officeNumber,
            roadNameAddress = roadNameAddress,
        )
    )

    override suspend fun updateProfileImage(
        userType: String,
        imageFileUri: String,
    ): Result<Unit> = runCatching {
        getImageInputStream(context, imageFileUri.toUri())?.use { inputStream ->
            val imageFormat = getImageFormat(context, imageFileUri.toUri())

            val profileImageUploadUrlResponse = getProfileImageUploadUrl(
                userType = userType,
                imageFileExtension = imageFormat.name,
            ).getOrThrow()

            uploadProfileImage(
                uploadUrl = profileImageUploadUrlResponse.uploadUrl,
                imageFileExtension = profileImageUploadUrlResponse.imageFileExtension,
                imageInputStream = inputStream,
            ).getOrThrow()

            callbackImageUpload(
                userType = userType,
                imageId = profileImageUploadUrlResponse.imageId,
                imageFileExtension = profileImageUploadUrlResponse.imageFileExtension
            )
        }
    }

    private fun getImageFormat(context: Context, uri: Uri): MIMEType {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri)
        return MIMEType.create(mimeType)
    }

    private fun getImageInputStream(context: Context, uri: Uri): InputStream? {
        return context.contentResolver.openInputStream(uri)
    }

    private suspend fun getProfileImageUploadUrl(
        userType: String,
        imageFileExtension: String
    ): Result<UploadProfileImageUrlResponse> =
        profileDataSource.getProfileImageUploadUrl(userType, imageFileExtension)

    private suspend fun uploadProfileImage(
        uploadUrl: String,
        imageFileExtension: String,
        imageInputStream: InputStream,
    ): Result<Unit> = profileDataSource.uploadProfileImage(
        uploadUrl = uploadUrl,
        imageFileExtension = imageFileExtension,
        imageInputStream = imageInputStream,
    )

    private suspend fun callbackImageUpload(
        userType: String,
        imageId: String,
        imageFileExtension: String
    ): Result<Unit> = profileDataSource.callbackImageUpload(
        userType = userType,
        callbackImageUploadRequest = CallbackImageUploadRequest(
            imageId = imageId,
            imageFileExtension = imageFileExtension,
        )
    )
}