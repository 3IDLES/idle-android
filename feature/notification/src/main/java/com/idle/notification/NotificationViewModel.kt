package com.idle.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.domain.model.error.ErrorHandler
import com.idle.domain.model.notification.Notification
import com.idle.domain.usecase.notification.GetMyNotificationUseCase
import com.idle.domain.usecase.notification.ReadNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getMyNotificationUseCase: GetMyNotificationUseCase,
    private val readNotificationUseCase: ReadNotificationUseCase,
    private val errorHandlerHelper: ErrorHandler,
    private val navigationHelper: com.idle.navigation.NavigationHelper,
) : ViewModel() {
    private val next = MutableStateFlow<String?>(null)

    private val _callType = MutableStateFlow(NotificationCallType.NOTIFICATION)

    private val _myNotifications = MutableStateFlow<List<Notification>?>(null)
    val myNotification = _myNotifications.asStateFlow()

    internal fun getMyNotifications() = viewModelScope.launch {
        if (_callType.value == NotificationCallType.END) {
            return@launch
        }

        getMyNotificationUseCase(next.value).onSuccess { (nextId, notifications) ->
            _myNotifications.value = _myNotifications.value?.plus(notifications) ?: notifications
            next.value = nextId

            if (nextId == null) {
                _callType.value = NotificationCallType.END
            }
        }.onFailure { errorHandlerHelper.sendError(it) }
    }

    internal fun onNotificationClick(notification: Notification) = viewModelScope.launch {
        launch {
            readNotificationUseCase(notification.id).onSuccess {
                _myNotifications.value = _myNotifications.value?.map {
                    if (it.id == notification.id) {
                        notification.copy(isRead = true)
                    } else {
                        it
                    }
                }
            }.onFailure {
                errorHandlerHelper.sendError(it)
            }
        }

        navigationHelper.handleNotificationNavigate(notification)
    }
}

enum class NotificationCallType {
    NOTIFICATION, END
}