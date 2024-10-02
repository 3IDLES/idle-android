package com.idle.care.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.idle.domain.model.auth.UserType
import com.idle.domain.usecase.auth.GetUserTypeUseCase
import com.idle.domain.usecase.notification.PostDeviceTokenUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var postDeviceTokenUseCase: PostDeviceTokenUseCase

    @Inject
    lateinit var getUserTypeUseCase: GetUserTypeUseCase

    @Inject
    lateinit var notificationHandler: NotificationHandler

    private val job = SupervisorJob()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("test", throwable.stackTraceToString())
    }
    private val scope = CoroutineScope(Dispatchers.IO + job + coroutineExceptionHandler)

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        scope.launch {
            val userType = getUserTypeUseCase()

            when (userType) {
                UserType.CENTER.apiValue, UserType.WORKER.apiValue -> postDeviceTokenUseCase(
                    deviceToken = token,
                    userType = userType,
                )
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: "케어밋"
        val body = message.notification?.body ?: ""
//        val data = message.data
        val data = mapOf(
            "destination" to "JOB_POSTING_DETAIL",
            "jobPostingId" to "01920467-e697-7167-aee8-9c5477c2ce0e"
        )
        notificationHandler.deliverNotification(
            title = title,
            body = body,
            data = data,
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}