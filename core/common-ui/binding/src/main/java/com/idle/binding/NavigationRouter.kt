package com.idle.binding

import android.os.Bundle
import com.idle.binding.DeepLinkDestination.CenterApplicantInquiry
import com.idle.binding.DeepLinkDestination.CenterHome
import com.idle.binding.DeepLinkDestination.CenterJobDetail
import com.idle.binding.DeepLinkDestination.WorkerJobDetail
import com.idle.domain.model.jobposting.JobPostingType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationRouter @Inject constructor() {
    private val _navigationFlow = Channel<NavigationEvent>(BUFFERED)
    val navigationFlow = _navigationFlow.receiveAsFlow()

    fun navigateTo(navigationEvent: NavigationEvent) {
        _navigationFlow.trySend(navigationEvent)
    }

    fun handleNotificationNavigate(
        isColdStart: Boolean,
        extras: Bundle?,
        onInit: () -> Unit
    ) {
        val notificationType = NotificationType.create(
            extras?.getString(NotificationKeys.NOTIFICATION_TYPE) ?: run {
                if (isColdStart) {
                    onInit()
                    return
                }
                return
            }
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
                        NavigationEvent.NavigateTo(CenterApplicantInquiry(jobPostingId)),
                    )
                } else {
                    listOf(
                        NavigationEvent.NavigateTo(CenterJobDetail(jobPostingId)),
                        NavigationEvent.NavigateTo(CenterApplicantInquiry(jobPostingId)),
                    )
                }

                destinations.forEach { destination -> _navigationFlow.trySend(destination) }
            }

            NotificationType.JOB_POSTING_DETAIL -> {
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
}

sealed class NavigationEvent {
    data class NavigateTo(val destination: DeepLinkDestination, val popUpTo: Int? = null) :
        NavigationEvent()

    data class NavigateToAuthWithClearBackStack(val snackBarMsg: String) : NavigationEvent()
}

enum class NotificationType {
    APPLICANT,
    JOB_POSTING_DETAIL,
    UNKNOWN;

    companion object {
        fun create(type: String): NotificationType {
            return NotificationType.entries.firstOrNull { it.name == type } ?: UNKNOWN
        }
    }
}

private object NotificationKeys {
    const val NOTIFICATION_TYPE = "notificationType"
    const val JOB_POSTING_ID = "jobPostingId"
}
