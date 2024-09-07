package com.idle.network.source.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.google.firebase.remoteconfig.get
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RemoteConfigDataSource @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
) {
    suspend fun getValue(key: String): FirebaseRemoteConfigValue? =
        suspendCancellableCoroutine { continuation ->
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(remoteConfig[key])
                } else {
                    task.exception?.let { continuation.resumeWithException(it) }
                        ?: continuation.resume(null)
                }
            }
        }

    suspend fun getString(key: String, defaultValue: String): String =
        getValue(key)?.asString() ?: defaultValue
}