package com.idle.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.idle.datastore.util.getValue
import com.idle.datastore.util.setValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named

class MapDataVersionDataSource @Inject constructor(
    @Named("mapDataVersion") private val dataStore: DataStore<Preferences>
) {
    val dataVersion: Flow<String> = dataStore.getValue(DATA_VERSION, "")
    val isTimeToLiveValid: Flow<Boolean> = dataStore.getValue(TIME_TO_LIVE, "")
        .map { timeToLiveString ->
            if (timeToLiveString.isNotEmpty()) {
                val timeToLive = LocalDateTime.parse(
                    timeToLiveString,
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                )
                LocalDateTime.now(seoulZone).isBefore(timeToLive)
            } else {
                false
            }
        }

    suspend fun setDataVersion(dataVersion: String) {
        dataStore.setValue(DATA_VERSION, dataVersion)
    }

    suspend fun setTimeToLive(interval: Int) {
        val timeToLive = LocalDateTime.now(seoulZone).plusSeconds(interval.toLong())
        val timeToLiveString = timeToLive.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        dataStore.setValue(TIME_TO_LIVE, timeToLiveString)
    }

    companion object {
        private val seoulZone = ZoneId.of("Asia/Seoul")
        private val DATA_VERSION = stringPreferencesKey("DATA_VERSION")
        private val TIME_TO_LIVE = stringPreferencesKey("TIME_TO_LIVE")
    }
}