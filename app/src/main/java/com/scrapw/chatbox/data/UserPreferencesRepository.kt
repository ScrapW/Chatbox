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
        const val ERROR_READ = "Error reading preferences."

        val IP_ADDRESS = stringPreferencesKey("ip_address")
        val IS_REALTIME_MSG = booleanPreferencesKey("is_realtime_msg")
    }

    val ipAddress = getString(IP_ADDRESS, "127.0.0.1")

    suspend fun saveIpAddress(value: String) {
        saveString(IP_ADDRESS, value)
    }

    val isRealtimeMsg = getBoolean(IS_REALTIME_MSG, false)

    suspend fun saveIsRealtimeMsg(value: Boolean) {
        saveBoolean(IS_REALTIME_MSG, value)
    }

    private fun getString(key: Preferences.Key<String>, defaultValue: String): Flow<String> {
        return dataStore.data
            .catch {
                if (it is IOException) {
                    Log.e(TAG, ERROR_READ, it)
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    private suspend fun saveString(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    private fun getBoolean(key: Preferences.Key<Boolean>, defaultValue: Boolean): Flow<Boolean> {
        return dataStore.data
            .catch {
                if (it is IOException) {
                    Log.e(TAG, ERROR_READ, it)
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    private suspend fun saveBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}
