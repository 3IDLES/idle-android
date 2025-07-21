package com.idle.care.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.idle.designsystem.binding.R
import com.idle.domain.model.error.ErrorHelper
import com.idle.domain.repositorry.ProfileRepository
import com.idle.domain.repositorry.TokenRepository
import com.idle.presentation.MainActivity
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
    lateinit var errorHelper: ErrorHelper

    private val notificationManager: NotificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        errorHelper.logError(throwable)
    }
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler)

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        scope.launch {
            val userType = profileRepository.getMyUserType()

            if (userType.isNotEmpty()) {
                tokenRepository.postDeviceToken(
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
        val data = message.data

        deliverNotification(
            title = title,
            body = body,
            data = data,
        )
    }

    internal fun deliverNotification(
        title: String,
        body: String,
        data: Map<String, String>
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        if (data.isNotEmpty()) {
            data.forEach { (key, value) ->
                intent.putExtra(key, value)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val builder = NotificationCompat.Builder(this, BACKGROUND_CHANNEL)
            .setSmallIcon(com.idle.care.R.drawable.ic_notification_icon)
            .setColor(ContextCompat.getColor(this, R.color.orange_500))
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {
        private const val BACKGROUND_CHANNEL = "백그라운드 알림"
        private const val BACKGROUND_DESCRIPTION =
            "센터장 : 공고 지원자 확인, 요양보호사 : 희망 공고가 게시되었을 때의 알림을 받을 수 있는 채널입니다."

        fun initNotification(context: Context) {
            val channel =
                NotificationChannel(
                    BACKGROUND_CHANNEL,
                    BACKGROUND_CHANNEL,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            channel.description = BACKGROUND_DESCRIPTION

            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
