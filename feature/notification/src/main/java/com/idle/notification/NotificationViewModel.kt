package com.idle.notification

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.domain.usecase.notification.GetNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationUseCase: GetNotificationUseCase,
) : BaseViewModel() {
    init {
        viewModelScope.launch {
            getNotificationUseCase().onSuccess {
                Log.d("test", it.toString())
            }
        }
    }
}