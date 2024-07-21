package com.idle.data.repository.profile

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.MIMEType
import com.idle.domain.repositorry.profile.ProfileRepository
import com.idle.network.model.profile.CallbackImageUploadRequest
import com.idle.network.model.profile.CenterProfileRequest
import com.idle.network.model.profile.ProfileImageUploadUrlResponse
import com.idle.network.source.CenterProfileDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val centerProfileDataSource: CenterProfileDataSource,
    @ApplicationContext private val context: Context,
) : ProfileRepository {
    override suspend fun getMyCenterProfile(): Result<CenterProfile> =
        centerProfileDataSource.getMyCenterProfile().mapCatching { it.toVO() }

    override suspend fun updateCenterProfile(
        officeNumber: String,
        introduce: String?,
    ): Result<Unit> = centerProfileDataSource.updateMyCenterProfile(
        CenterProfileRequest(
            officeNumber = officeNumber,
            introduce = introduce,
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
    ): Result<ProfileImageUploadUrlResponse> =
        centerProfileDataSource.getProfileImageUploadUrl(userType, imageFileExtension)

    private suspend fun uploadProfileImage(
        uploadUrl: String,
        imageFileExtension: String,
        imageInputStream: InputStream,
    ): Result<Unit> = centerProfileDataSource.uploadProfileImage(
        uploadUrl = uploadUrl,
        imageFileExtension = imageFileExtension,
        imageInputStream = imageInputStream,
    )

    private suspend fun callbackImageUpload(
        userType: String,
        imageId: String,
        imageFileExtension: String
    ): Result<Unit> = centerProfileDataSource.callbackImageUpload(
        userType = userType,
        callbackImageUploadRequest = CallbackImageUploadRequest(
            imageId = imageId,
            imageFileExtension = imageFileExtension,
        )
    )
}