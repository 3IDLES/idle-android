package com.idle.data.repository.auth

import com.idle.datastore.datasource.TokenDataSource
import com.idle.datastore.datasource.UserInfoDataSource
import com.idle.domain.model.auth.UserType
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.domain.repositorry.auth.TokenRepository
import com.idle.domain.repositorry.profile.ProfileRepository
import com.idle.network.model.token.TokenResponse
import com.idle.network.source.auth.AuthDataSource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthRepositoryImplTest {

    private lateinit var authDataSource: AuthDataSource
    private lateinit var tokenDataSource: TokenDataSource
    private lateinit var userInfoDataSource: UserInfoDataSource
    private lateinit var authRepository: AuthRepository
    private lateinit var tokenRepository: TokenRepository
    private lateinit var profileRepository: ProfileRepository

    @BeforeEach
    fun setUp() {
        authDataSource = mockk()
        tokenDataSource = mockk()
        userInfoDataSource = mockk()
        tokenRepository = mockk()
        profileRepository = mockk()
        authRepository = AuthRepositoryImpl(
            authDataSource = authDataSource,
            tokenDataSource = tokenDataSource,
            userInfoDataSource = userInfoDataSource,
            profileRepository = profileRepository,
            tokenRepository = tokenRepository,
        )

        coEvery { tokenDataSource.setAccessToken(any()) } just Runs
        coEvery { tokenDataSource.setRefreshToken(any()) } just Runs
        coEvery { userInfoDataSource.setUserType(any()) } just Runs
        coEvery { userInfoDataSource.setUserInfo(any()) } just Runs
        coEvery { tokenDataSource.clearToken() } just Runs
        coEvery { userInfoDataSource.clearUserType() } just Runs
        coEvery { userInfoDataSource.clearUserInfo() } just Runs
        coEvery { authDataSource.getDeviceToken() } returns "testToken"
        coEvery { tokenRepository.postDeviceToken(any(), any()) } returns Result.success(Unit)
        coEvery { tokenRepository.deleteDeviceToken(any()) } returns Result.success(Unit)
    }

    @Test
    fun `센터장이 로그인 성공했을 경우 토큰을 저장하고 디바이스 토큰을 서버로 보낸다`() = runTest {
        // Given
        val tokenResponse = TokenResponse("accessToken", "refreshToken")
        val centerProfile = mockk<CenterProfile>()
        coEvery { authDataSource.signInCenter(any()) } returns Result.success(tokenResponse)
        coEvery { profileRepository.getMyCenterProfile() } returns Result.success(centerProfile)

        // When
        val result = authRepository.signInCenter("identifier", "password")

        // Then
        assertTrue(result.isSuccess)
        coVerify { tokenDataSource.setAccessToken("accessToken") }
        coVerify { tokenDataSource.setRefreshToken("refreshToken") }
        coVerify { userInfoDataSource.setUserType(UserType.CENTER.apiValue) }
        coVerify { tokenRepository.postDeviceToken("testToken", "CENTER") }
    }

    @Test
    fun `요양보호사가 회원가입에 성공했을 경우 토큰을 저장하고 디바이스 토큰을 서버로 보낸다`() = runTest {
        // Given
        val tokenResponse = TokenResponse("accessToken", "refreshToken")
        val workerProfile = mockk<WorkerProfile>()
        coEvery { authDataSource.signUpWorker(any()) } returns Result.success(tokenResponse)
        coEvery { profileRepository.getMyWorkerProfile() } returns Result.success(workerProfile)

        // When
        val result = authRepository.signUpWorker("name", 1990, "male", "01012345678", "road", "lot")

        // Then
        assertTrue(result.isSuccess)
        coVerify { tokenDataSource.setAccessToken("accessToken") }
        coVerify { tokenDataSource.setRefreshToken("refreshToken") }
        coVerify { userInfoDataSource.setUserType(UserType.WORKER.apiValue) }
        coVerify { tokenRepository.postDeviceToken("testToken", "CARER") }
    }

    @Test
    fun `요양보호사가 로그아웃했을 경우 토큰을 제거한다`() = runTest {
        // Given
        coEvery { authDataSource.logoutWorker() } returns Result.success(Unit)

        // When
        val result = authRepository.logoutWorker()

        // Then
        assertTrue(result.isSuccess)
        coVerify { tokenDataSource.clearToken() }
        coVerify { tokenRepository.deleteDeviceToken(any()) }
        coVerify { userInfoDataSource.clearUserType() }
        coVerify { userInfoDataSource.clearUserInfo() }
    }

    @Test
    fun `요양보호사가 회원탈퇴했을 경우 토큰을 제거한다`() = runTest {
        // Given
        coEvery { authDataSource.withdrawalWorker(any()) } returns Result.success(Unit)

        // When
        val result = authRepository.withdrawalWorker("reason")

        // Then
        assertTrue(result.isSuccess)
        coVerify { tokenDataSource.clearToken() }
        coVerify { tokenRepository.deleteDeviceToken(any()) }
        coVerify { userInfoDataSource.clearUserType() }
        coVerify { userInfoDataSource.clearUserInfo() }
    }

    @Test
    fun `센터장이 로그아웃했을 경우 토큰을 제거한다`() = runTest {
        // Given
        coEvery { authDataSource.logoutCenter() } returns Result.success(Unit)

        // When
        val result = authRepository.logoutCenter()

        // Then
        assertTrue(result.isSuccess)
        coVerify { tokenDataSource.clearToken() }
        coVerify { tokenRepository.deleteDeviceToken(any()) }
        coVerify { userInfoDataSource.clearUserType() }
        coVerify { userInfoDataSource.clearUserInfo() }
    }

    @Test
    fun `센터장이 회원탈퇴했을 경우 토큰을 제거한다`() = runTest {
        // Given
        coEvery { authDataSource.withdrawalCenter(any()) } returns Result.success(Unit)

        // When
        val result = authRepository.withdrawalCenter("reason", "password")

        // Then
        assertTrue(result.isSuccess)
        coVerify { tokenDataSource.clearToken() }
        coVerify { tokenRepository.deleteDeviceToken(any()) }
        coVerify { userInfoDataSource.clearUserType() }
        coVerify { userInfoDataSource.clearUserInfo() }
    }
}
