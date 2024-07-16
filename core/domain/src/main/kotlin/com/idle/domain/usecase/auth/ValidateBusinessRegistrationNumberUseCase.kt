package com.idle.domain.usecase.auth

import com.idle.domain.model.auth.BusinessRegistrationInfo
import com.idle.domain.repositorry.auth.AuthRepository
import javax.inject.Inject

class ValidateBusinessRegistrationNumberUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(businessRegistrationNumber: String): Result<BusinessRegistrationInfo> =
        runCatching {
            val formattedNumber = when (businessRegistrationNumber.length) {
                10 -> formatTenDigitNumber(businessRegistrationNumber)
                12 -> businessRegistrationNumber
                else -> throw IllegalArgumentException("사업자 등록번호 형식이 맞지 않습니다.")
            }

            return authRepository.validateBusinessRegistrationNumber(formattedNumber)
        }

    private fun formatTenDigitNumber(number: String): String {
        return number.replaceFirst(
            Regex("(\\d{3})(\\d{2})(\\d{5})"),
            "$1-$2-$3"
        )
    }
}