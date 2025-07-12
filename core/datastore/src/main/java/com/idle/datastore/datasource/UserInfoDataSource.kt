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
    val userInfo: Flow<String> = dataStore.getValue(USER_INFO, "")

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

    suspend fun getLocalCenterProfile(): CenterProfile {
        val userInfoString = userInfo.first().takeIf { it.isNotBlank() }
            ?: throw NullPointerException("Missing UserInfo")

        if (!userInfoString.startsWith("CenterProfile(")) {
            throw NullPointerException("Stored UserInfo is not a CenterProfile")
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
            centerId = properties["centerId"] ?: throw NullPointerException("Missing CenterId"),
            centerName = properties["centerName"]
                ?: throw NullPointerException("Missing centerName"),
            officeNumber = properties["officeNumber"]
                ?: throw NullPointerException("Missing officeNumber"),
            roadNameAddress = properties["roadNameAddress"]
                ?: throw NullPointerException("Missing roadNameAddress"),
            lotNumberAddress = properties["lotNumberAddress"]
                ?: throw NullPointerException("Missing lotNumberAddress"),
            detailedAddress = properties["detailedAddress"]
                ?: throw NullPointerException("Missing detailedAddress"),
            longitude = properties["longitude"]?.toDoubleOrNull()
                ?: throw NullPointerException("Invalid longitude format"),
            latitude = properties["latitude"]?.toDoubleOrNull()
                ?: throw NullPointerException("Invalid latitude format"),
            introduce = properties["introduce"].takeIf { it != "null" },
            profileImageUrl = properties["profileImageUrl"].takeIf { it != "null" },
        )
    }

    suspend fun getLocalWorkerProfile(): WorkerProfile {
        val userInfoString = userInfo.first().takeIf { it.isNotBlank() }
            ?: throw NullPointerException("Missing UserInfo")

        if (!userInfoString.startsWith("WorkerProfile(")) {
            throw NullPointerException("Stored UserInfo is not a WorkerProfile")
        }

        val properties = userInfoString
            .removePrefix("WorkerProfile(")
            .removeSuffix(")")
            .split(", ")
            .associate {
                val (key, value) = it.split("=")
                key to value
            }

        return WorkerProfile(
            workerId = properties["workerId"] ?: throw NullPointerException("Missing workerId"),
            workerName = properties["workerName"]
                ?: throw NullPointerException("Missing workerName"),
            age = properties["age"]?.toInt() ?: throw NullPointerException("Invalid age format"),
            gender = Gender.create(properties["gender"]),
            experienceYear = properties["experienceYear"]?.toIntOrNull(),
            phoneNumber = properties["phoneNumber"]
                ?: throw NullPointerException("Missing phoneNumber"),
            roadNameAddress = properties["roadNameAddress"]
                ?: throw NullPointerException("Missing roadNameAddress"),
            lotNumberAddress = properties["lotNumberAddress"]
                ?: throw NullPointerException("Missing lotNumberAddress"),
            longitude = properties["longitude"] ?: throw NullPointerException("Missing longitude"),
            latitude = properties["latitude"] ?: throw NullPointerException("Missing latitude"),
            jobSearchStatus = JobSearchStatus.create(properties["jobSearchStatus"]),
            introduce = properties["introduce"].takeIf { it != "null" },
            speciality = properties["speciality"].takeIf { it != "null" },
            profileImageUrl = properties["profileImageUrl"].takeIf { it != "null" }
        )
    }

    companion object {
        private val USER_TYPE = stringPreferencesKey("USER_TYPE")
        private val USER_INFO = stringPreferencesKey("USER_INFO")
    }
}
