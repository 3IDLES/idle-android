package com.idle.notification

import androidx.lifecycle.viewModelScope
import com.idle.binding.DeepLinkDestination.CenterApplicantInquiry
import com.idle.binding.DeepLinkDestination.CenterJobDetail
import com.idle.binding.NavigationEvent
import com.idle.binding.NavigationHelper
import com.idle.binding.base.BaseViewModel
import com.idle.domain.model.error.ErrorHandlerHelper
import com.idle.domain.model.notification.Notification
import com.idle.domain.model.notification.NotificationContent
import com.idle.domain.model.notification.NotificationType
import com.idle.domain.usecase.notification.GetMyNotificationUseCase
import com.idle.domain.usecase.notification.ReadNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getMyNotificationUseCase: GetMyNotificationUseCase,
    private val readNotificationUseCase: ReadNotificationUseCase,
    private val errorHandlerHelper: ErrorHandlerHelper,
    private val navigationHelper: NavigationHelper,
) : BaseViewModel() {
    private val next = MutableStateFlow<String?>(null)

    private val _callType =
        MutableStateFlow<NotificationCallType>(NotificationCallType.NOTIFICATION)

    private val myNotifications = MutableStateFlow<List<Notification>?>(null)

    val todayNotification = myNotifications.map { list ->
        list?.filter { it.daysSinceCreation() < 1 }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null,
    )

    val weeklyNotification = myNotifications.map { list ->
        list?.filter { it.daysSinceCreation() in 1 until 7 }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null,
    )

    val monthlyNotification = myNotifications.map { list ->
        list?.filter { it.daysSinceCreation() in 7 until 30 }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null,
    )

    internal fun getMyNotifications() = viewModelScope.launch {
        if (_callType.value == NotificationCallType.END) {
            return@launch
        }

        getMyNotificationUseCase(next.value).onSuccess { (nextId, notifications) ->
            myNotifications.value = myNotifications.value?.plus(notifications) ?: notifications
            next.value = nextId

            if (nextId == null) {
                _callType.value = NotificationCallType.END
            }
        }.onFailure { errorHandlerHelper.sendError(it) }
    }

    internal fun onNotificationClick(notification: Notification) = viewModelScope.launch {
        launch {
            readNotificationUseCase(notification.id).onFailure {
                errorHandlerHelper.sendError(it)
            }
        }

        handleNotificationDestination(notification)
    }

    private fun handleNotificationDestination(notification: Notification) {
        val screenDepth = when (notification.notificationType) {
            NotificationType.APPLICANT -> {
                val notificationContent =
                    notification.notificationDetails as? NotificationContent.ApplicantNotification

                notificationContent?.let { content ->
                    listOf(
                        CenterJobDetail(content.jobPostingId),
                        CenterApplicantInquiry(content.jobPostingId)
                    )
                } ?: listOf()
            }

            else -> listOf()
        }

        screenDepth.onEach { screen ->
            navigationHelper.navigateTo(NavigationEvent.NavigateTo(screen))
        }
    }
}

enum class NotificationCallType {
    NOTIFICATION, END
}