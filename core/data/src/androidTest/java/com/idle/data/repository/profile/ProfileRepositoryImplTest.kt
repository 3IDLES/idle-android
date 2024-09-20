package com.idle.data.repository.profile

import com.idle.datastore.datasource.UserInfoDataSource
import com.idle.network.model.profile.UploadProfileImageUrlResponse
import com.idle.network.source.profile.ProfileDataSource
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(MockitoJUnitRunner::class)
class ProfileRepositoryImplTest {

    private lateinit var profileDataSource: ProfileDataSource
    private lateinit var userInfoDataSource: UserInfoDataSource
    private lateinit var profileRepository: ProfileRepositoryImpl

    @Before
    fun setUp() {
        profileDataSource = mockk()
        userInfoDataSource = mockk()
        profileRepository = ProfileRepositoryImpl(
            profileDataSource = profileDataSource,
            userInfoDataSource = userInfoDataSource,
            context = mockk()
        )
    }

    @Test
    fun `프로필 이미지 업로드 PresignedUrl이 순차적으로 동작한다`() = runBlockingTest {
        // Given
        val uploadUrlResponse = UploadProfileImageUrlResponse(
            uploadUrl = "uploadUrl",
            imageId = "imageId",
            imageFileExtension = "jpeg"
        )

        val mockInputStream = mockk<InputStream>(relaxed = true)
        val mockContext = mockk<Context>(relaxed = true)
        val contentResolver = mockk<ContentResolver>(relaxed = true)

        // Mock the context and input stream
        every { mockContext.contentResolver } returns contentResolver
        every { contentResolver.openInputStream(any()) } returns mockInputStream
        coEvery { profileDataSource.getProfileImageUploadUrl(any(), any()) } returns Result.success(
            uploadUrlResponse
        )
        coEvery {
            profileDataSource.uploadProfileImage(
                any(),
                any(),
                any()
            )
        } returns Result.success(Unit)
        coEvery { profileDataSource.callbackImageUpload(any(), any()) } returns Result.success(Unit)

        // When
        val result = profileRepository.updateProfileImage(
            userType = UserType.CENTER.apiValue,
            imageFileUri = "file://test-image"
        )

        // Then
        assertTrue(result.isSuccess)
        coVerify { profileDataSource.getProfileImageUploadUrl(any(), any()) }
        coVerify { profileDataSource.uploadProfileImage(any(), any(), any()) }
        coVerify { profileDataSource.callbackImageUpload(any(), any()) }
    }
}
