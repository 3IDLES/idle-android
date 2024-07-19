package com.idle.center.profile

import com.idle.binding.base.BaseViewModel
import com.idle.domain.usecase.profile.GetMyCenterProfileUseCase
import com.idle.domain.usecase.profile.UpdateMyCenterProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CenterProfileViewModel @Inject constructor(
    private val getMyCenterProfileUseCase: GetMyCenterProfileUseCase,
    private val updateMyCenterProfileUseCase: UpdateMyCenterProfileUseCase,
) : BaseViewModel() {
}