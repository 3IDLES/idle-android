package com.idle.domain.usecase.profile

import com.idle.domain.repositorry.profile.CenterProfileRepository
import javax.inject.Inject

class UpdateMyCenterProfileUseCase @Inject constructor(
    private val centerProfileRepository: CenterProfileRepository
) {
    suspend operator fun invoke(
        officeNumber: String,
        introduce: String?,
    ) = centerProfileRepository.updateMyCenterProfile(
        officeNumber = officeNumber,
        introduce = introduce,
    )
}