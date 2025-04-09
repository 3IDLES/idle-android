package com.idle.care.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.repositorry.TokenRepository
import com.idle.domain.repositorry.ProfileRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var tokenRepository: TokenRepository

    @Inject
    lateinit var profileRepository: ProfileRepository

    @Inject
    lateinit var notificationHandler: NotificationHandler

    @Inject
    lateinit var errorHelper: ErrorHelper

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        errorHelper.logError(throwable)
    }
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler)

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        scope.launch {
            val userType = profileRepository.getMyUserType()

            tokenRepository.postDeviceToken(
                deviceToken = token,
                userType = userType,
            )
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: "케어밋"
        val body = message.notification?.body ?: ""
        val data = message.data

        notificationHandler.deliverNotification(
            title = title,
            body = body,
            data = data,
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
