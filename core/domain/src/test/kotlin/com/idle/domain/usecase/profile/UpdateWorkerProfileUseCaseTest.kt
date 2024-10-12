package com.idle.domain.usecase.profile

import com.idle.domain.model.auth.UserType
import com.idle.domain.model.profile.JobSearchStatus
import com.idle.domain.repositorry.profile.ProfileRepository
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateWorkerProfileUseCaseTest {

    private lateinit var profileRepository: ProfileRepository
    private lateinit var useCase: UpdateWorkerProfileUseCase

    private val experienceYear = 5
    private val roadNameAddress = "서울시 강남구"
    private val lotNumberAddress = "123-456"
    private val introduce = "안녕하세요."
    private val speciality = "간병"
    private val jobSearchStatus = JobSearchStatus.YES

    @BeforeEach
    fun setUp() {
        profileRepository = mockk(relaxed = true)
        useCase = UpdateWorkerProfileUseCase(profileRepository)
    }

    @AfterEach
    fun tearDown() {
        clearMocks(profileRepository)
    }

    @Test
    fun `이미지 파일이 content로 시작하면 프로필과 이미지 업데이트가 호출된다`() = runTest {
        // Given: 이미지 파일이 content://로 시작
        val imageFileUri = "content://media/external/images/media/12345"

        // When
        useCase(
            experienceYear = experienceYear,
            roadNameAddress = roadNameAddress,
            lotNumberAddress = lotNumberAddress,
            introduce = introduce,
            speciality = speciality,
            jobSearchStatus = jobSearchStatus,
            imageFileUri = imageFileUri
        )

        // Then
        coVerify { profileRepository.updateWorkerProfile(any(), any(), any(), any(), any(), any()) }
        coVerify {
            profileRepository.updateProfileImage(
                userType = UserType.WORKER.apiValue,
                imageFileUri = imageFileUri,
                reqWidth = 384,
                reqHeight = 384
            )
        }
    }

    @Test
    fun `이미지 파일이 https로 시작하면 이미지 업데이트는 호출되지 않고 프로필만 업데이트된다`() = runTest {
        // Given: 이미지 파일이 https://로 시작
        val imageFileUri = "https://example.com/path/to/image"

        // When
        useCase(
            experienceYear = experienceYear,
            roadNameAddress = roadNameAddress,
            lotNumberAddress = lotNumberAddress,
            introduce = introduce,
            speciality = speciality,
            jobSearchStatus = jobSearchStatus,
            imageFileUri = imageFileUri
        )

        // Then
        coVerify { profileRepository.updateWorkerProfile(any(), any(), any(), any(), any(), any()) }
        coVerify(exactly = 0) {
            profileRepository.updateProfileImage(any(), any(), any(), any())
        }
    }

    @Test
    fun `이미지 파일이 null이면 프로필만 업데이트되고 이미지 업데이트는 호출되지 않는다`() = runTest {
        // Given: 이미지 파일이 null
        val imageFileUri: String? = null

        // When
        useCase(
            experienceYear = experienceYear,
            roadNameAddress = roadNameAddress,
            lotNumberAddress = lotNumberAddress,
            introduce = introduce,
            speciality = speciality,
            jobSearchStatus = jobSearchStatus,
            imageFileUri = imageFileUri
        )

        // Then
        coVerify { profileRepository.updateWorkerProfile(any(), any(), any(), any(), any(), any()) }
        coVerify(exactly = 0) {
            profileRepository.updateProfileImage(any(), any(), any(), any())
        }
    }
}
