package com.idle.domain.usecase.profile

import com.idle.domain.repositorry.profile.CenterProfileRepository
import javax.inject.Inject

class GetMyCenterProfileUseCase @Inject constructor(
    private val centerProfileRepository: CenterProfileRepository
) {
    suspend operator fun invoke() = centerProfileRepository.getMyCenterProfile()
}