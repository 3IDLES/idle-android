package com.idle.care.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.idle.designsystem.binding.R
import com.idle.presentation.MainActivity
import javax.inject.Inject

class NotificationHandler @Inject constructor(private val context: Context) {
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    internal fun deliverNotification(
        title: String,
        body: String,
        data: Map<String, String>
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        if (data.isNotEmpty()) {
            data.forEach { (key, value) ->
                intent.putExtra(key, value)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val builder = NotificationCompat.Builder(context, BACKGROUND_CHANNEL)
            .setSmallIcon(com.idle.care.R.drawable.ic_notification_icon)
            .setColor(ContextCompat.getColor(context, R.color.orange_500))
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    companion object {
        const val BACKGROUND_CHANNEL = "백그라운드 알림"
        const val BACKGROUND_DESCRIPTION =
            "센터장 : 공고 지원자 확인, 요양보호사 : 희망 공고가 게시되었을 때의 알림을 받을 수 있는 채널입니다."
    }
}