package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SignUpCenterUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var signUpCenterUseCase: SignUpCenterUseCase

    @BeforeEach
    fun setUp() {
        authRepository = mockk()
        signUpCenterUseCase = SignUpCenterUseCase(authRepository)

        coEvery {
            authRepository.signUpCenter(
                identifier = any(),
                password = any(),
                phoneNumber = any(),
                managerName = any(),
                businessRegistrationNumber = any(),
            )
        } returns Result.success(Unit)
    }

    private val identifier = "testId"
    private val password = "testPassword"
    private val phoneNumber = "01012345678"
    private val managerName = "Test Manager"

    @Test
    fun `사업자 등록번호가 10자리일 때 포맷이 적용된다`() = runTest {
        // Given
        val businessRegistrationNumber = "1234567890"
        val formattedNumber = "123-45-67890"

        // When
        val result = signUpCenterUseCase(
            identifier = identifier,
            password = password,
            phoneNumber = phoneNumber,
            managerName = managerName,
            businessRegistrationNumber = businessRegistrationNumber
        )

        // Then
        assertTrue(result.isSuccess)
        coVerify {
            authRepository.signUpCenter(
                identifier = identifier,
                password = password,
                phoneNumber = any(),
                managerName = managerName,
                businessRegistrationNumber = formattedNumber
            )
        }
    }

    @Test
    fun `사업자 등록번호가 12자리일 때 {3}-{2}-{5} 형식이 올바르면 성공한다`() = runTest {
        // Given
        val businessRegistrationNumber = "123-45-67890"

        // When
        val result = signUpCenterUseCase(
            identifier = identifier,
            password = password,
            phoneNumber = phoneNumber,
            managerName = managerName,
            businessRegistrationNumber = businessRegistrationNumber
        )

        // Then
        assertTrue(result.isSuccess)
        coVerify {
            authRepository.signUpCenter(
                identifier = identifier,
                password = password,
                phoneNumber = any(),
                managerName = managerName,
                businessRegistrationNumber = businessRegistrationNumber
            )
        }
    }

    @Test
    fun `사업자 등록번호가 12자리일 때 {3}-{2}-{5} 형식이 아니면 예외가 발생한다`() {
        // Given
        val businessRegistrationNumber = "123-456-7890"  // 잘못된 포맷

        // When & Then
        assertThrows<IllegalArgumentException> {
            runTest {
                signUpCenterUseCase(
                    identifier = identifier,
                    password = password,
                    phoneNumber = phoneNumber,
                    managerName = managerName,
                    businessRegistrationNumber = businessRegistrationNumber
                )
            }
        }
    }

    @Test
    fun `사업자 등록번호가 10자리나 12자리가 아닌 경우 예외를 발생시킨다`() {
        // Given
        val businessRegistrationNumber = "12345678"  // 10자리나 12자리가 아님

        // When & Then
        assertThrows<IllegalArgumentException> {
            runTest {
                signUpCenterUseCase(
                    identifier = identifier,
                    password = password,
                    phoneNumber = phoneNumber,
                    managerName = managerName,
                    businessRegistrationNumber = businessRegistrationNumber
                )
            }
        }
    }
}