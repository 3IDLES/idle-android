package com.idle.data.repository.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.net.toUri
import com.idle.datastore.datasource.UserInfoDataSource
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.CenterRegistrationStatus
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource,
    private val userInfoDataSource: UserInfoDataSource,
    @ApplicationContext private val context: Context,
) : ProfileRepository {
    override suspend fun getMyUserRole() = withContext(Dispatchers.IO) {
        userInfoDataSource.userRole.first()
    }

    override suspend fun getMyCenterProfile(): Result<CenterProfile> =
        profileDataSource.getMyCenterProfile()
            .mapCatching { it.toVO() }
            .onSuccess {
                userInfoDataSource.setUserInfo(it.toString())
                Result.success(it)
            }

    override suspend fun getLocalMyCenterProfile(): Result<CenterProfile> = runCatching {
        val userInfoString = withContext(Dispatchers.IO) {
            userInfoDataSource.userInfo.first()
                .takeIf { it.isNotBlank() } ?: throw IllegalArgumentException("Missing UserInfo")
        }

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
            introduce = properties["introduce"].takeIf { it != "null" },
            profileImageUrl = properties["profileImageUrl"].takeIf { it != "null" }
        )
    }

    override suspend fun getCenterProfile(centerId: String): Result<CenterProfile> =
        profileDataSource.getCenterProfile(centerId).mapCatching { it.toVO() }

    override suspend fun getMyWorkerProfile(): Result<WorkerProfile> =
        profileDataSource.getMyWorkerProfile().mapCatching { it.toVo() }
            .onSuccess {
                userInfoDataSource.setUserInfo(it.toString())
                Result.success(it)
            }

    override suspend fun getLocalMyWorkerProfile(): Result<WorkerProfile> = runCatching {
        val userInfoString = withContext(Dispatchers.IO) {
            userInfoDataSource.userInfo.first()
                .takeIf { it.isNotBlank() } ?: throw IllegalArgumentException("Missing UserInfo")
        }

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
            introduce = properties["introduce"].takeIf { it != "null" },
            speciality = properties["speciality"].takeIf { it != "null" },
            profileImageUrl = properties["profileImageUrl"].takeIf { it != "null" },
        )
    }

    override suspend fun getWorkerProfile(workerId: String): Result<WorkerProfile> =
        profileDataSource.getWorkerProfile(workerId).mapCatching { it.toVo() }

    override suspend fun updateCenterProfile(
        officeNumber: String,
        introduce: String?,
    ): Result<Unit> = profileDataSource.updateMyCenterProfile(
        UpdateCenterProfileRequest(officeNumber = officeNumber, introduce = introduce)
    ).onSuccess {
        val updatedProfile = getMyCenterProfile().getOrNull()
        if (updatedProfile != null) {
            userInfoDataSource.setUserInfo(updatedProfile.toString())
        }
    }

    override suspend fun getWorkerId(): Result<String> = profileDataSource.getWorkerId()
        .mapCatching { it.carerId }

    override suspend fun getCenterStatus(): Result<CenterRegistrationStatus> =
        profileDataSource.getCenterStatus()
            .mapCatching { it.toVO() }

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
    ).onSuccess {
        val updatedProfile = getMyWorkerProfile().getOrNull()
        if (updatedProfile != null) {
            userInfoDataSource.setUserInfo(updatedProfile.toString())
        }
    }

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
        reqWidth: Int,
        reqHeight: Int
    ): Result<Unit> = runCatching {
        resizeImage(
            context = context,
            uri = imageFileUri.toUri(),
            reqWidth = reqWidth,
            reqHeight = reqHeight,
        ).use { inputStream ->
            val imageFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                MIMEType.WEBP
            } else MIMEType.JPG

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
            ).getOrThrow()

            when (userType) {
                UserType.CENTER.apiValue -> {
                    val updatedProfile = getMyCenterProfile().getOrThrow()
                        .copy(profileImageUrl = imageFileUri)

                    userInfoDataSource.setUserInfo(updatedProfile.toString())
                }

                UserType.WORKER.apiValue -> {
                    val updatedProfile = getMyWorkerProfile().getOrThrow()
                        .copy(profileImageUrl = imageFileUri)

                    userInfoDataSource.setUserInfo(updatedProfile.toString())
                }
            }
        }
    }

    private fun resizeImage(
        context: Context,
        uri: Uri,
        reqWidth: Int,
        reqHeight: Int
    ): InputStream {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            options.inJustDecodeBounds = false

            context.contentResolver.openInputStream(uri)?.use { newInputStream ->
                val resizedBitmap = BitmapFactory.decodeStream(newInputStream, null, options)

                val byteArrayOutputStream = ByteArrayOutputStream()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    resizedBitmap?.compress(
                        Bitmap.CompressFormat.WEBP_LOSSY, 100, byteArrayOutputStream
                    )
                } else {
                    resizedBitmap?.compress(
                        Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream
                    )
                }
                val byteArray = byteArrayOutputStream.toByteArray()

                return ByteArrayInputStream(byteArray)
            }
        }

        throw IllegalArgumentException("Unable to open InputStream for the given URI")
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
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