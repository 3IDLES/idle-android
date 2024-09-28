package com.idle.care.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.idle.domain.model.auth.UserType
import com.idle.domain.usecase.auth.GetUserRoleUseCase
import com.idle.domain.usecase.auth.UpdateDeviceTokenUseCase
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
    lateinit var updateDeviceTokenUseCase: UpdateDeviceTokenUseCase

    @Inject
    lateinit var getUserRoleUseCase: GetUserRoleUseCase

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
            val userRole = getUserRoleUseCase()

            when (userRole) {
                UserType.CENTER.apiValue, UserType.WORKER.apiValue -> updateDeviceTokenUseCase(token)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: message.data["title"] ?: ""
        val body = message.notification?.body ?: message.data["body"] ?: ""

        notificationHandler.deliverNotification(
            title = title,
            body = body,
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}