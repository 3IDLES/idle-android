package com.idle.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.net.toUri
import com.idle.datastore.datasource.UserInfoDataSource
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.CenterRegistrationStatus
import com.idle.domain.model.profile.JobSearchStatus
import com.idle.domain.model.profile.MIMEType
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.repositorry.ProfileRepository
import com.idle.network.model.profile.CallbackImageUploadRequest
import com.idle.network.model.profile.RegisterCenterProfileRequest
import com.idle.network.model.profile.UpdateCenterProfileRequest
import com.idle.network.model.profile.UpdateWorkerProfileRequest
import com.idle.network.model.profile.UploadProfileImageUrlResponse
import com.idle.network.source.ProfileDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource,
    private val userInfoDataSource: UserInfoDataSource,
    @ApplicationContext private val context: Context,
) : ProfileRepository {
    override suspend fun getMyUserType() = userInfoDataSource.userType.first()

    override suspend fun getMyCenterProfile(): CenterProfile {
        val centerProfile = profileDataSource.getMyCenterProfile()
            .toVO()

        userInfoDataSource.setUserInfo(centerProfile.toString())
        return centerProfile
    }

    override suspend fun getLocalMyCenterProfile(): CenterProfile =
        userInfoDataSource.getLocalCenterProfile()

    override suspend fun getCenterProfile(centerId: String): CenterProfile =
        profileDataSource.getCenterProfile(centerId).toVO()

    override suspend fun getMyWorkerProfile(): WorkerProfile {
        val workerProfile = profileDataSource.getMyWorkerProfile().toVo()

        userInfoDataSource.setUserInfo(workerProfile.toString())
        return workerProfile
    }

    override suspend fun getLocalMyWorkerProfile(): WorkerProfile =
        userInfoDataSource.getLocalWorkerProfile()

    override suspend fun getWorkerProfile(workerId: String): WorkerProfile =
        profileDataSource.getWorkerProfile(workerId).toVo()

    override suspend fun updateCenterProfile(
        officeNumber: String,
        introduce: String?,
    ) {
        profileDataSource.updateMyCenterProfile(
            UpdateCenterProfileRequest(officeNumber = officeNumber, introduce = introduce)
        )

        val updatedProfile = getMyCenterProfile()
        userInfoDataSource.setUserInfo(updatedProfile.toString())
    }

    override suspend fun getWorkerId(): String = profileDataSource.getWorkerId()
        .carerId

    override suspend fun getCenterStatus(): CenterRegistrationStatus =
        profileDataSource.getCenterStatus().toVO()

    override suspend fun updateWorkerProfile(
        experienceYear: Int?,
        roadNameAddress: String,
        lotNumberAddress: String,
        jobSearchStatus: JobSearchStatus,
        introduce: String?,
        speciality: String
    ) {
        profileDataSource.updateWorkerProfile(
            UpdateWorkerProfileRequest(
                experienceYear = experienceYear,
                roadNameAddress = roadNameAddress,
                lotNumberAddress = lotNumberAddress,
                jobSearchStatus = jobSearchStatus.name,
                introduce = introduce,
                speciality = speciality
            )
        )

        val updatedProfile = getMyWorkerProfile()
        userInfoDataSource.setUserInfo(updatedProfile.toString())
    }

    override suspend fun registerCenterProfile(
        centerName: String,
        detailedAddress: String,
        introduce: String,
        lotNumberAddress: String,
        officeNumber: String,
        roadNameAddress: String
    ) = profileDataSource.registerCenterProfile(
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
    ) {
        val resizeImage = resizeImage(
            context = context,
            uri = imageFileUri.toUri(),
            reqWidth = reqWidth,
            reqHeight = reqHeight,
        )

        resizeImage.use { inputStream ->
            val imageFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                MIMEType.WEBP
            } else MIMEType.JPG

            val profileImageUploadUrlResponse = getProfileImageUploadUrl(
                userType = userType,
                imageFileExtension = imageFormat.name,
            )

            uploadProfileImage(
                uploadUrl = profileImageUploadUrlResponse.uploadUrl,
                imageFileExtension = profileImageUploadUrlResponse.imageFileExtension,
                imageInputStream = inputStream,
            )

            callbackImageUpload(
                userType = userType,
                imageId = profileImageUploadUrlResponse.imageId,
                imageFileExtension = profileImageUploadUrlResponse.imageFileExtension
            )

            when (userType) {
                UserType.CENTER.apiValue -> {
                    val updatedProfile = getMyCenterProfile()
                        .copy(profileImageUrl = imageFileUri)

                    userInfoDataSource.setUserInfo(updatedProfile.toString())
                }

                UserType.WORKER.apiValue -> {
                    val updatedProfile = getMyWorkerProfile()
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
        val originImageStream = context.contentResolver.openInputStream(uri)

        originImageStream?.use { inputStream ->
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
    ): UploadProfileImageUrlResponse =
        profileDataSource.getProfileImageUploadUrl(userType, imageFileExtension)

    private suspend fun uploadProfileImage(
        uploadUrl: String,
        imageFileExtension: String,
        imageInputStream: InputStream,
    ) = profileDataSource.uploadProfileImage(
        uploadUrl = uploadUrl,
        imageFileExtension = imageFileExtension,
        imageInputStream = imageInputStream,
    )

    private suspend fun callbackImageUpload(
        userType: String,
        imageId: String,
        imageFileExtension: String
    ) = profileDataSource.callbackImageUpload(
        userType = userType,
        callbackImageUploadRequest = CallbackImageUploadRequest(
            imageId = imageId,
            imageFileExtension = imageFileExtension,
        )
    )
}
