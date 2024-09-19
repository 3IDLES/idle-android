package com.idle.data.repository.auth

import com.idle.datastore.datasource.TokenDataSource
import com.idle.datastore.datasource.UserInfoDataSource
import com.idle.domain.model.auth.UserType
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
    private lateinit var authRepository: AuthRepositoryImpl

    @BeforeEach
    fun setUp() {
        authDataSource = mockk()
        tokenDataSource = mockk()
        userInfoDataSource = mockk()
        authRepository = AuthRepositoryImpl(authDataSource, tokenDataSource, userInfoDataSource)

        coEvery { tokenDataSource.setAccessToken(any()) } just Runs
        coEvery { tokenDataSource.setRefreshToken(any()) } just Runs
        coEvery { userInfoDataSource.setUserRole(any()) } just Runs
        coEvery { tokenDataSource.clearToken() } just Runs
        coEvery { userInfoDataSource.clearUserRole() } just Runs
        coEvery { userInfoDataSource.clearUserInfo() } just Runs
    }

    @Test
    fun `센터장이 로그인 성공했을 경우 토큰을 저장한다`() = runTest {
        // Given
        val tokenResponse = TokenResponse("accessToken", "refreshToken")
        coEvery { authDataSource.signInCenter(any()) } returns Result.success(tokenResponse)

        // When
        val result = authRepository.signInCenter("identifier", "password")

        // Then
        assertTrue(result.isSuccess)
        coVerify { tokenDataSource.setAccessToken("accessToken") }
        coVerify { tokenDataSource.setRefreshToken("refreshToken") }
        coVerify { userInfoDataSource.setUserRole(UserType.CENTER.apiValue) }
    }

    @Test
    fun `요양보호사가 회원가입에 성공했을 경우 토큰을 저장한다`() = runTest {
        // Given
        val tokenResponse = TokenResponse("accessToken", "refreshToken")
        coEvery { authDataSource.signUpWorker(any()) } returns Result.success(tokenResponse)

        // When
        val result = authRepository.signUpWorker("name", 1990, "male", "01012345678", "road", "lot")

        // Then
        assertTrue(result.isSuccess)
        coVerify { tokenDataSource.setAccessToken("accessToken") }
        coVerify { tokenDataSource.setRefreshToken("refreshToken") }
        coVerify { userInfoDataSource.setUserRole(UserType.WORKER.apiValue) }
    }

    @Test
    fun `요양보호사가 로그인했을 경우 토큰을 저장한다`() = runTest {
        // Given
        val tokenResponse = TokenResponse("accessToken", "refreshToken")
        coEvery { authDataSource.signInWorker(any()) } returns Result.success(tokenResponse)

        // When
        val result = authRepository.signInWorker("01012345678", "authCode")

        // Then
        assertTrue(result.isSuccess)
        coVerify { tokenDataSource.setAccessToken("accessToken") }
        coVerify { tokenDataSource.setRefreshToken("refreshToken") }
        coVerify { userInfoDataSource.setUserRole(UserType.WORKER.apiValue) }
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
        coVerify { userInfoDataSource.clearUserRole() }
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
        coVerify { userInfoDataSource.clearUserRole() }
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
        coVerify { userInfoDataSource.clearUserRole() }
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
        coVerify { userInfoDataSource.clearUserRole() }
        coVerify { userInfoDataSource.clearUserInfo() }
    }
}
