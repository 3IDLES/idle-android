package com.idle.network.source.remoteconfig

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.google.firebase.remoteconfig.get
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ConfigDataSource @Inject constructor(
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

    suspend inline fun <reified T> getReferenceType(key: String): T? {
        val json = Json {
            ignoreUnknownKeys = true
        }

        return getString(key, "").let {
            runCatching { json.decodeFromString<T>(it) }.getOrNull()
        }
    }

    suspend inline fun <reified T> getReferenceType(key: String, defaultValue: T): T {
        return getReferenceType<T>(key) ?: defaultValue
    }

    companion object Key {
        const val FORCE_UPDATE = "forceUpdate"
    }
}