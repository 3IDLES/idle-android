package com.idle.navigation

import android.os.Bundle
import com.idle.domain.model.jobposting.JobPostingType
import com.idle.domain.model.notification.Notification
import com.idle.domain.model.notification.NotificationContent
import com.idle.domain.model.notification.NotificationType.APPLICANT
import com.idle.domain.model.notification.NotificationType.NEW_JOB_POSTING
import com.idle.navigation.DeepLinkDestination.CenterHome
import com.idle.navigation.DeepLinkDestination.CenterJobDetail
import com.idle.navigation.DeepLinkDestination.WorkerJobDetail
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationHelper @Inject constructor() {
    private val _navigationFlow = Channel<NavigationEvent>(BUFFERED)
    val navigationFlow = _navigationFlow.receiveAsFlow()

    fun navigateTo(navigationEvent: NavigationEvent) {
        _navigationFlow.trySend(navigationEvent)
    }

    fun handleFCMNavigate(
        isColdStart: Boolean,
        extras: Bundle?,
        onInit: () -> Unit,
        readNotification: (String) -> Unit,
    ) {
        val notificationId = extras?.getString(NotificationKeys.NOTIFICATION_ID) ?: run {
            if (isColdStart) onInit()
            return
        }

        readNotification(notificationId)

        val notificationType = NotificationType.create(
            extras.getString(NotificationKeys.NOTIFICATION_TYPE) ?: return
        )

        when (notificationType) {
            NotificationType.APPLICANT -> {
                val jobPostingId = extras.getString(NotificationKeys.JOB_POSTING_ID) ?: run {
                    if (isColdStart) {
                        onInit()
                        return
                    }
                    return
                }

                val destinations = if (isColdStart) {
                    listOf(
                        NavigationEvent.NavigateTo(CenterHome),
                        NavigationEvent.NavigateTo(CenterJobDetail(jobPostingId)),
                    )
                } else {
                    listOf(
                        NavigationEvent.NavigateTo(CenterJobDetail(jobPostingId)),
                    )
                }

                destinations.forEach { destination -> _navigationFlow.trySend(destination) }
            }

            NotificationType.NEW_JOB_POSTING -> {
                val jobPostingId = extras.getString(NotificationKeys.JOB_POSTING_ID) ?: run {
                    if (isColdStart) {
                        onInit()
                        return
                    }
                    return
                }

                val destinations = listOf(
                    NavigationEvent.NavigateTo(DeepLinkDestination.WorkerHome),
                    NavigationEvent.NavigateTo(
                        WorkerJobDetail(
                            jobPostingId,
                            JobPostingType.CAREMEET.name
                        )
                    )
                )

                destinations.forEach { destination -> _navigationFlow.trySend(destination) }
            }

            NotificationType.UNKNOWN -> return
        }
    }

    fun handleNotificationNavigate(notification: Notification) {
        val destinations = when (notification.notificationType) {
            APPLICANT -> {
                (notification.notificationDetails as? NotificationContent.ApplicantNotification)?.let { content ->
                    listOf(NavigationEvent.NavigateTo(CenterJobDetail(content.jobPostingId)))
                } ?: listOf()
            }

            NEW_JOB_POSTING -> {
                (notification.notificationDetails as? NotificationContent.NewJobPostingNotification)?.let { content ->
                    listOf(
                        NavigationEvent.NavigateTo(
                            WorkerJobDetail(
                                content.jobPostingId,
                                JobPostingType.CAREMEET.name
                            )
                        )
                    )
                } ?: listOf()
            }

            else -> listOf()
        }
        destinations.forEach { _navigationFlow.trySend(it) }
    }
}

sealed class NavigationEvent {
    data class NavigateTo(val destination: DeepLinkDestination, val popUpTo: Int? = null) :
        NavigationEvent()

    data class NavigateToAuthWithClearBackStack(
        val toastMsg: String,
        val toastType: String = "ERROR",
    ) : NavigationEvent()
}

enum class NotificationType {
    APPLICANT,
    NEW_JOB_POSTING,
    UNKNOWN;

    companion object {
        fun create(type: String): NotificationType {
            return NotificationType.entries.firstOrNull { it.name == type } ?: UNKNOWN
        }
    }
}

private object NotificationKeys {
    const val NOTIFICATION_ID = "notificationId"
    const val NOTIFICATION_TYPE = "notificationType"
    const val JOB_POSTING_ID = "jobPostingId"
}
