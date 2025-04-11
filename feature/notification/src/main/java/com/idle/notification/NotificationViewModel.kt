package com.idle.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.model.notification.Notification
import com.idle.domain.model.notification.NotificationContent
import com.idle.domain.model.notification.NotificationType
import com.idle.domain.repositorry.NotificationRepository
import com.idle.navigation.DeepLinkDestination
import com.idle.navigation.NavigationEvent
import com.idle.navigation.NavigationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val errorHelper: ErrorHelper,
    private val navigationHelper: NavigationHelper,
) : ViewModel() {
    private val next = MutableStateFlow<String?>(null)

    private val _callType = MutableStateFlow(NotificationCallType.NOTIFICATION)

    private val _myNotifications = MutableStateFlow<List<Notification>?>(null)
    val myNotification = _myNotifications.asStateFlow()

    internal fun getMyNotifications() = viewModelScope.launch {
        if (_callType.value == NotificationCallType.END) {
            return@launch
        }

        notificationRepository.getMyNotifications(next.value).onSuccess { (nextId, notifications) ->
            _myNotifications.value = _myNotifications.value?.plus(notifications) ?: notifications
            next.value = nextId

            if (nextId == null) {
                _callType.value = NotificationCallType.END
            }
        }.onFailure { errorHelper.sendError(it) }
    }

    internal fun onNotificationClick(notification: Notification) = viewModelScope.launch {
        launch {
            notificationRepository.readNotification(notification.id).onSuccess {
                _myNotifications.value = _myNotifications.value?.map {
                    if (it.id == notification.id) {
                        notification.copy(isRead = true)
                    } else {
                        it
                    }
                }
            }.onFailure {
                errorHelper.sendError(it)
            }
        }

        handleNotificationNavigate(notification)
    }

    private fun handleNotificationNavigate(notification: Notification) {
        val destinations = when (notification.notificationType) {
            NotificationType.APPLICANT -> {
                (notification.notificationDetails as? NotificationContent.ApplicantNotification)
                    ?.let { DeepLinkDestination.CenterJobDetail(it.jobPostingId) }
                    ?.let { listOf(it) } ?: listOf()
            }

            NotificationType.NEW_JOB_POSTING -> {
                (notification.notificationDetails as? NotificationContent.NewJobPostingNotification)
                    ?.let {
                        DeepLinkDestination.WorkerJobDetail(
                            it.jobPostingId,
                            JobPostingType.CAREMEET.name
                        )
                    }
                    ?.let { listOf(it) } ?: listOf()
            }

            else -> listOf()
        }
        destinations.forEach { destination ->
            navigationHelper.navigateTo(NavigationEvent.To(destination))
        }
    }
}

enum class NotificationCallType {
    NOTIFICATION, END
}
