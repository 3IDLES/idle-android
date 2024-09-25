package com.idle.network.source.notification

import com.idle.network.api.UserApi
import com.idle.network.util.safeApiCall
import javax.inject.Inject

class NotificationDataSource @Inject constructor(
    private val userApi: UserApi,
) {
    suspend fun getNotifications() = safeApiCall { userApi.getNotification() }
}