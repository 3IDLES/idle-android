package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.domain.util.formatPhoneNumber
import javax.inject.Inject

class SignUpCenterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        identifier: String,
        password: String,
        phoneNumber: String,
        managerName: String,
        businessRegistrationNumber: String,
    ): Result<Unit> {
        val formattedPhoneNumber = formatPhoneNumber(phoneNumber)
        val formattedNumber = when (businessRegistrationNumber.length) {
            10 -> formatTenDigitNumber(businessRegistrationNumber)
            12 -> businessRegistrationNumber
            else -> throw IllegalArgumentException("사업자 등록번호 형식이 맞지 않습니다.")
        }

        return authRepository.signUpCenter(
            identifier = identifier,
            password = password,
            phoneNumber = formattedPhoneNumber,
            managerName = managerName,
            businessRegistrationNumber = formattedNumber,
        )
    }

    private fun formatTenDigitNumber(number: String): String {
        return number.replaceFirst(
            Regex("(\\d{3})(\\d{2})(\\d{5})"),
            "$1-$2-$3"
        )
    }
}