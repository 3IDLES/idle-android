package com.idle.domain.usecase.profile

import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.repositorry.ProfileRepository
import javax.inject.Inject

class GetMyWorkerProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): WorkerProfile = try {
        profileRepository.getLocalMyWorkerProfile()
    } catch (e: Exception) {
        profileRepository.getMyWorkerProfile()
    }
}
