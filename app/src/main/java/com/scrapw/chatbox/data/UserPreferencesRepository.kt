package com.scrapw.chatbox.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private companion object {
        const val TAG = "UserPreferencesRepo"

        val IP_ADDRESS = stringPreferencesKey("ip_address")
        val IS_REALTIME_MSG = booleanPreferencesKey("is_realtime_msg")
    }

    val ipAddress: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[IP_ADDRESS] ?: "127.0.0.1"
        }

    suspend fun saveIpAddress(ipAddress: String) {
        dataStore.edit { preferences ->
            preferences[IP_ADDRESS] = ipAddress
        }
    }

    val isRealtimeMsg: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[IS_REALTIME_MSG] ?: false
        }

    suspend fun saveIsRealtimeMsg(isRealtimeMsg: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_REALTIME_MSG] = isRealtimeMsg
        }
    }
}
