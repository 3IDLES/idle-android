package com.idle.notification

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.usecase.notification.GetMyNotificationUseCase
import com.idle.domain.usecase.notification.ReadNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getMyNotificationUseCase: GetMyNotificationUseCase,
    private val readNotificationUseCase: ReadNotificationUseCase,
) : BaseViewModel() {
    init {
        viewModelScope.launch {
            getMyNotificationUseCase().onSuccess {
                Log.d("test", it.toString())
            }
        }
    }

    internal fun readNotification(notificationId: String) = viewModelScope.launch {
        readNotificationUseCase(notificationId).onSuccess {

        }.onFailure {
            handleFailure(it as HttpResponseException)
        }
    }
}