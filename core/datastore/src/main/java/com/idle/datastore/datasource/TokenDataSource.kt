package com.idle.datastore.datasource

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.idle.datastore.util.getValue
import com.idle.datastore.util.setValue
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class TokenDataSource @Inject constructor(
    @Named("token") private val dataStore: DataStore<Preferences>
) {
    val accessToken: Flow<String> = dataStore.getValue(ACCESS_TOKEN, "")
    val refreshToken: Flow<String> = dataStore.getValue(REFRESH_TOKEN, "")

    suspend fun setAccessToken(accessToken: String) {
        dataStore.setValue(ACCESS_TOKEN, accessToken)
    }

    suspend fun setRefreshToken(refreshToken: String) {
        dataStore.setValue(REFRESH_TOKEN, refreshToken)
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        private val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
    }
}