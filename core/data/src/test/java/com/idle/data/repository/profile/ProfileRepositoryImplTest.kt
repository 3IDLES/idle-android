package com.idle.data.repository.profile

import com.idle.datastore.datasource.UserInfoDataSource
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.Gender
import com.idle.domain.model.profile.JobSearchStatus
import com.idle.domain.model.profile.WorkerProfile
import com.idle.network.model.profile.GetCenterProfileResponse
import com.idle.network.model.profile.GetWorkerProfileResponse
import com.idle.network.source.profile.ProfileDataSource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProfileRepositoryImplTest {

    private lateinit var profileDataSource: ProfileDataSource
    private lateinit var userInfoDataSource: UserInfoDataSource
    private lateinit var profileRepository: ProfileRepositoryImpl

    private val centerProfile = CenterProfile(
        centerName = "Test Center",
        officeNumber = "010-1234-5678",
        roadNameAddress = "Address",
        lotNumberAddress = "Address2",
        detailedAddress = "Detail",
        longitude = 127.0,
        latitude = 37.0,
        introduce = null,
        profileImageUrl = null,
    )

    private val workerProfile = WorkerProfile(
        workerName = "Test Worker",
        age = 30,
        gender = Gender.MAN,
        experienceYear = 5,
        phoneNumber = "010-1234-5678",
        roadNameAddress = "Address",
        lotNumberAddress = "Address2",
        longitude = "127.0",
        latitude = "37.0",
        jobSearchStatus = JobSearchStatus.YES,
        introduce = null,
        speciality = null,
        profileImageUrl = null,
    )

    private val getCenterProfileResponse = GetCenterProfileResponse(
        centerName = "Test Center",
        officeNumber = "010-1234-5678",
        roadNameAddress = "Address",
        lotNumberAddress = "Address2",
        detailedAddress = "Detail",
        longitude = 127.0,
        latitude = 37.0,
        introduce = null,
        profileImageUrl = null
    )

    private val getWorkerProfileResponse = GetWorkerProfileResponse(
        workerName = "Test Worker",
        age = 30,
        gender = "MALE",
        experienceYear = 5,
        phoneNumber = "010-1234-5678",
        roadNameAddress = "Address",
        lotNumberAddress = "Address2",
        longitude = "127.0",
        latitude = "37.0",
        jobSearchStatus = "YES",
        introduce = null,
        speciality = null,
        profileImageUrl = null
    )

    @BeforeEach
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
    fun `UserInfo가 없을 때 IllegalArgumentException을 발생시킨다`() = runTest {
        // Given
        coEvery { userInfoDataSource.userInfo } returns flowOf("")

        // When & Then
        assertThrows<IllegalArgumentException> {
            profileRepository.getLocalMyCenterProfile().getOrThrow()
        }

        assertThrows<IllegalArgumentException> {
            profileRepository.getLocalMyWorkerProfile().getOrThrow()
        }
    }

    @Test
    fun `센터 프로필이 로컬 캐시된 후 성공적으로 반환된다`() = runTest {
        // Given: 로컬 캐시에 저장된 센터 프로필 문자열을 반환하도록 설정
        coEvery { userInfoDataSource.userInfo } returns flowOf(centerProfile.toString())

        // When: 로컬에서 센터 프로필을 가져오는 함수 호출
        val centerResult = profileRepository.getLocalMyCenterProfile().getOrNull()

        // Then: 각 필드를 개별적으로 비교하여 프로필이 정확히 반환되었는지 확인
        assertNotNull(centerResult)
        assertEquals(centerProfile.centerName, centerResult?.centerName)
        assertEquals(centerProfile.officeNumber, centerResult?.officeNumber)
        assertEquals(centerProfile.roadNameAddress, centerResult?.roadNameAddress)
        assertEquals(centerProfile.lotNumberAddress, centerResult?.lotNumberAddress)
        assertEquals(centerProfile.detailedAddress, centerResult?.detailedAddress)
        assertEquals(centerProfile.longitude, centerResult?.longitude)
        assertEquals(centerProfile.latitude, centerResult?.latitude)
        assertEquals(centerProfile.introduce, centerResult?.introduce)
        assertEquals(centerProfile.profileImageUrl, centerResult?.profileImageUrl)
    }

    @Test
    fun `요양보호사 프로필이 로컬 캐시된 후 성공적으로 반환된다`() = runTest {
        // Given: 로컬 캐시에 저장된 요양보호사 프로필 문자열을 반환하도록 설정
        coEvery { userInfoDataSource.userInfo } returns flowOf(workerProfile.toString())

        // When: 로컬에서 요양보호사 프로필을 가져오는 함수 호출
        val workerResult = profileRepository.getLocalMyWorkerProfile().getOrNull()

        // Then: 각 필드를 개별적으로 비교하여 프로필이 정확히 반환되었는지 확인
        assertNotNull(workerResult)
        assertEquals(workerProfile.workerName, workerResult?.workerName)
        assertEquals(workerProfile.age, workerResult?.age)
        assertEquals(workerProfile.gender, workerResult?.gender)
        assertEquals(workerProfile.experienceYear, workerResult?.experienceYear)
        assertEquals(workerProfile.phoneNumber, workerResult?.phoneNumber)
        assertEquals(workerProfile.roadNameAddress, workerResult?.roadNameAddress)
        assertEquals(workerProfile.lotNumberAddress, workerResult?.lotNumberAddress)
        assertEquals(workerProfile.longitude, workerResult?.longitude)
        assertEquals(workerProfile.latitude, workerResult?.latitude)
        assertEquals(workerProfile.jobSearchStatus, workerResult?.jobSearchStatus)
        assertEquals(workerProfile.introduce, workerResult?.introduce)
        assertEquals(workerProfile.speciality, workerResult?.speciality)
        assertEquals(workerProfile.profileImageUrl, workerResult?.profileImageUrl)
    }


    @Test
    fun `프로필 업데이트 성공 시 로컬 캐시된다`() = runTest {
        // Given
        coEvery { profileDataSource.updateMyCenterProfile(any()) } returns Result.success(Unit)
        coEvery { userInfoDataSource.setUserInfo(any()) } just Runs
        coEvery { profileDataSource.getMyCenterProfile() } returns Result.success(
            getCenterProfileResponse
        )

        // When
        val result = profileRepository.updateCenterProfile(
            officeNumber = "010-1234-5678",
            introduce = "New introduction"
        )

        // Then
        assertTrue(result.isSuccess)
        coVerify { userInfoDataSource.setUserInfo(any()) }

        // Given for worker
        coEvery { profileDataSource.updateWorkerProfile(any()) } returns Result.success(Unit)
        coEvery { profileDataSource.getMyWorkerProfile() } returns Result.success(
            getWorkerProfileResponse
        )

        // When
        val workerResult = profileRepository.updateWorkerProfile(
            experienceYear = 5,
            roadNameAddress = "Address",
            lotNumberAddress = "Address2",
            jobSearchStatus = JobSearchStatus.YES,
            introduce = "Intro",
            speciality = "Speciality"
        )

        // Then
        assertTrue(workerResult.isSuccess)
        coVerify { userInfoDataSource.setUserInfo(any()) }
    }

    @Test
    fun `프로필 조회 성공 시 로컬 캐시된다`() = runTest {
        // Given
        coEvery { profileDataSource.getMyCenterProfile() } returns Result.success(
            getCenterProfileResponse
        )
        coEvery { profileDataSource.getMyWorkerProfile() } returns Result.success(
            getWorkerProfileResponse
        )
        coEvery { userInfoDataSource.setUserInfo(any()) } just Runs

        // When
        val centerResult = profileRepository.getMyCenterProfile()
        val workerResult = profileRepository.getMyWorkerProfile()

        // Then
        assertTrue(centerResult.isSuccess)
        assertTrue(workerResult.isSuccess)
        coVerify { userInfoDataSource.setUserInfo(any()) }
    }
}
