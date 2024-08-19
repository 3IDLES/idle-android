package com.idle.domain.usecase.auth

import com.idle.domain.repositorry.profile.ProfileRepository
import javax.inject.Inject

class GetUserRoleUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() = profileRepository.getMyUserRole()
}