package com.idle.notification

import androidx.lifecycle.viewModelScope
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.notification.Notification
import com.idle.domain.usecase.notification.GetMyNotificationUseCase
import com.idle.domain.usecase.notification.ReadNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getMyNotificationUseCase: GetMyNotificationUseCase,
    private val readNotificationUseCase: ReadNotificationUseCase,
) : BaseViewModel() {
    private val myNotifications = MutableStateFlow<List<Notification>>(
        listOf(
            Notification(""),
            Notification(""),
            Notification(""),
        )
    )

    val todayNotification = myNotifications.filter { true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList(),
        )

    val weeklyNotification = myNotifications.filter { true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList(),
        )

    val monthlyNotification = myNotifications.filter { true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList(),
        )

    internal fun getMyNotifications() = viewModelScope.launch {
        getMyNotificationUseCase().onSuccess { }
            .onFailure { }
    }

    internal fun clearNotifications() {
//        myNotifications.value = emptyList()
    }

    internal fun readNotification(notificationId: String) = viewModelScope.launch {
        readNotificationUseCase(notificationId).onSuccess {

        }.onFailure {
            handleFailure(it as HttpResponseException)
        }
    }
}