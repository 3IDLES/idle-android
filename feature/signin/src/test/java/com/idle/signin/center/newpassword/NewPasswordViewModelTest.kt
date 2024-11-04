package com.idle.signin.center.newpassword

import com.idle.binding.EventHandlerHelper
import com.idle.domain.model.CountDownTimer
import com.idle.domain.model.error.ErrorHandler
import com.idle.domain.usecase.auth.ConfirmAuthCodeUseCase
import com.idle.domain.usecase.auth.GenerateNewPasswordUseCase
import com.idle.domain.usecase.auth.SendPhoneNumberUseCase
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NewPasswordViewModelTest {
    private lateinit var sendPhoneNumberUseCase: SendPhoneNumberUseCase
    private lateinit var confirmAuthCodeUseCase: ConfirmAuthCodeUseCase
    private lateinit var generateNewPasswordUseCase: GenerateNewPasswordUseCase
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var errorHandlerHelper: ErrorHandler
    private lateinit var eventHandlerHelper: EventHandlerHelper
    private lateinit var navigationHelper: com.idle.navigation.NavigationHelper
    private lateinit var viewModel: NewPasswordViewModel
    private lateinit var testDispatcher: TestDispatcher

    @BeforeEach
    fun setup() {
        testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        sendPhoneNumberUseCase = mockk(relaxed = true)
        confirmAuthCodeUseCase = mockk(relaxed = true)
        generateNewPasswordUseCase = mockk(relaxed = true)
        countDownTimer = mockk(relaxed = true)
        errorHandlerHelper = mockk(relaxed = true)
        eventHandlerHelper = mockk(relaxed = true)
        navigationHelper = mockk(relaxed = true)

        viewModel = NewPasswordViewModel(
            sendPhoneNumberUseCase = sendPhoneNumberUseCase,
            confirmAuthCodeUseCase = confirmAuthCodeUseCase,
            generateNewPasswordUseCase = generateNewPasswordUseCase,
            countDownTimer = countDownTimer,
            errorHandlerHelper = errorHandlerHelper,
            eventHandlerHelper = eventHandlerHelper,
            navigationHelper = navigationHelper
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `비밀번호가 8자 이상 20자 이하일 때 유효성 검증을 통과하지 못한다`() = runTest {
        viewModel.setNewPassword("Test1234")
        testDispatcher.scheduler.runCurrent()

        val isPasswordLengthValid = viewModel.isPasswordLengthValid.first()
        assertTrue(isPasswordLengthValid)
    }

    @Test
    fun `비밀번호에 영문자와 숫자가 포함되지 않으면 유효성 검증을 통과하지 못한다`() = runTest {
        viewModel.setNewPassword("Password")
        testDispatcher.scheduler.runCurrent()

        val isPasswordContainsLetterAndDigit = viewModel.isPasswordContainsLetterAndDigit.first()
        assertFalse(isPasswordContainsLetterAndDigit)
    }

    @Test
    fun `비밀번호에 공백이 포함되어 있으면 유효성 검증을 통과하지 못한다`() = runTest {
        viewModel.setNewPassword("Test 1234")
        testDispatcher.scheduler.runCurrent()

        val isPasswordNoWhitespace = viewModel.isPasswordNoWhitespace.first()
        assertFalse(isPasswordNoWhitespace)
    }

    @Test
    fun `비밀번호에 3개 이상의 연속된 문자가 포함되어 있으면 유효성 검증을 통과하지 못한다`() = runTest {
        viewModel.setNewPassword("111Test1234")
        testDispatcher.scheduler.runCurrent()

        val isPasswordNoSequentialChars = viewModel.isPasswordNoSequentialChars.first()
        assertFalse(isPasswordNoSequentialChars)
    }
}
