package com.idle.care

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.idle.care.notification.NotificationHandler.Companion.BACKGROUND_CHANNEL
import com.idle.care.notification.NotificationHandler.Companion.BACKGROUND_DESCRIPTION
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class CareApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initNotification()
        initKakao()
        initAppsFlyer()
    }

    private fun initNotification() {
        val channel =
            NotificationChannel(
                BACKGROUND_CHANNEL,
                BACKGROUND_CHANNEL,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        channel.description = BACKGROUND_DESCRIPTION

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun initKakao() {
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
    }

    private fun initAppsFlyer() {
        AppsFlyerLib.getInstance().apply {
            init(BuildConfig.APPSFLYER_API_KEY, null, this@CareApplication)
            setDebugLog(true)
            start(this@CareApplication, "", object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    Log.d("AppsFlyer", "AppsFlyer 연동 성공")
                }

                override fun onError(p0: Int, p1: String) {
                    Log.d("AppsFlyer", "AppsFlyer 연동 실패 $p0 $p1")
                }
            })
        }
    }
}