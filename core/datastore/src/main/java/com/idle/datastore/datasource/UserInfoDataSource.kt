package com.idle.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.idle.datastore.util.clear
import com.idle.datastore.util.getValue
import com.idle.datastore.util.setValue
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.model.profile.JobSearchStatus
import com.idle.domain.model.profile.WorkerProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Named

class UserInfoDataSource @Inject constructor(
    @Named("userInfo") private val dataStore: DataStore<Preferences>
) {
    val userType: Flow<String> = dataStore.getValue(USER_TYPE, "")
    private val userInfo: Flow<String> = dataStore.getValue(USER_INFO, "")

    suspend fun setUserType(userRole: String) {
        dataStore.setValue(USER_TYPE, userRole)
    }

    suspend fun clearUserType() {
        dataStore.clear(USER_TYPE)
    }

    suspend fun setUserInfo(userInfo: String) {
        dataStore.setValue(USER_INFO, userInfo)
    }

    suspend fun clearUserInfo() {
        dataStore.clear(USER_INFO)
    }

    suspend fun getLocalCenterProfile(): CenterProfile? {
        try {
            val userInfoString = userInfo.first().takeIf { it.isNotBlank() }
                ?: return null

            if (!userInfoString.startsWith("CenterProfile(")) {
                return null
            }

            val properties = userInfoString
                .removePrefix("CenterProfile(")
                .removeSuffix(")")
                .split(", ")
                .associate {
                    val (key, value) = it.split("=")
                    key to value
                }

            return CenterProfile(
                centerId = properties["centerId"] ?: return null,
                centerName = properties["centerName"] ?: return null,
                officeNumber = properties["officeNumber"] ?: return null,
                roadNameAddress = properties["roadNameAddress"] ?: return null,
                lotNumberAddress = properties["lotNumberAddress"] ?: return null,
                detailedAddress = properties["detailedAddress"] ?: return null,
                longitude = properties["longitude"]?.toDoubleOrNull() ?: return null,
                latitude = properties["latitude"]?.toDoubleOrNull() ?: return null,
                introduce = properties["introduce"].takeIf { it != "null" },
                profileImageUrl = properties["profileImageUrl"].takeIf { it != "null" },
            )
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun getLocalWorkerProfile(): WorkerProfile? {
        return try {
            val userInfoString = userInfo.first()
                .takeIf { it.isNotBlank() } ?: return null

            if (!userInfoString.startsWith("WorkerProfile(")) return null

            val properties = userInfoString
                .removePrefix("WorkerProfile(")
                .removeSuffix(")")
                .split(", ")
                .associate {
                    val (key, value) = it.split("=")
                    key to value
                }

            WorkerProfile(
                workerId = properties["workerId"] ?: return null,
                workerName = properties["workerName"] ?: return null,
                age = properties["age"]?.toIntOrNull() ?: return null,
                gender = Gender.create(properties["gender"]),
                experienceYear = properties["experienceYear"]?.toIntOrNull(),
                phoneNumber = properties["phoneNumber"] ?: return null,
                roadNameAddress = properties["roadNameAddress"] ?: return null,
                lotNumberAddress = properties["lotNumberAddress"] ?: return null,
                longitude = properties["longitude"] ?: return null,
                latitude = properties["latitude"] ?: return null,
                jobSearchStatus = JobSearchStatus.create(properties["jobSearchStatus"]),
                introduce = properties["introduce"].takeIf { it != "null" },
                speciality = properties["speciality"].takeIf { it != "null" },
                profileImageUrl = properties["profileImageUrl"].takeIf { it != "null" }
            )
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private val USER_TYPE = stringPreferencesKey("USER_TYPE")
        private val USER_INFO = stringPreferencesKey("USER_INFO")
    }
}
