package com.idle.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.idle.datastore.util.clear
import com.idle.datastore.util.getValue
import com.idle.datastore.util.setValue
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class UserInfoDataSource @Inject constructor(
    @Named("userInfo") private val dataStore: DataStore<Preferences>
) {
    val userRole: Flow<String> = dataStore.getValue(USER_ROLE, "")
    val userInfo: Flow<String> = dataStore.getValue(USER_INFO, "")

    suspend fun setUserRole(userRole: String) {
        dataStore.setValue(USER_ROLE, userRole)
    }

    suspend fun clearUserRole(){
        dataStore.clear(USER_ROLE)
    }

    suspend fun setUserInfo(userInfo: String) {
        dataStore.setValue(USER_INFO, userInfo)
    }

    suspend fun clearUserInfo(){
        dataStore.clear(USER_INFO)
    }

    companion object {
        private val USER_ROLE = stringPreferencesKey("USER_ROLE")
        private val USER_INFO = stringPreferencesKey("USER_INFO")
    }
}