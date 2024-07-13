package com.idle.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.idle.datastore.util.handleException
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class TokenDataSource @Inject constructor(
    @Named("token") private val dataStore: DataStore<Preferences>
) {
    val accessToken = dataStore.data
        .handleException()
        .map { preferences ->
            preferences[ACCESS_TOKEN] ?: ""
        }

    val refreshToken = dataStore.data
        .handleException()
        .map { preferences ->
            preferences[REFRESH_TOKEN] ?: ""
        }

    suspend fun setAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun setRefreshToken(refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        private val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
    }
}