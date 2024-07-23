package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.auth.AuthRepository
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

    private fun formatPhoneNumber(phoneNumber: String): String {
        // 입력된 전화번호가 11자리 숫자인 경우에만 포맷팅을 적용
        return if (phoneNumber.length == 11) {
            phoneNumber.replaceFirst(
                Regex("(\\d{3})(\\d{4})(\\d{4})"),
                "$1-$2-$3"
            )
        } else {
            phoneNumber  // 비정상적인 경우 원본 번호 반환
        }
    }

    private fun formatTenDigitNumber(number: String): String {
        return number.replaceFirst(
            Regex("(\\d{3})(\\d{2})(\\d{5})"),
            "$1-$2-$3"
        )
    }
}