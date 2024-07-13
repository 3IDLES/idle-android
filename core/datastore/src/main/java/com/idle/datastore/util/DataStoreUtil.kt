package com.idle.datastore.util

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

internal fun <T> DataStore<Preferences>.getValue(
    key: Preferences.Key<T>,
    defaultValue: T
): Flow<T> =
    data.handleException()
        .map { preferences ->
            preferences[key] ?: defaultValue
        }

internal suspend fun <T> DataStore<Preferences>.setValue(
    key: Preferences.Key<T>,
    value: T,
) = edit { preferences ->
    preferences[key] = value
}


private fun Flow<Preferences>.handleException(): Flow<Preferences> =
    this.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            Log.e("DataStoreException", "Error accessing DataStore", exception)
            throw exception
        }
    }