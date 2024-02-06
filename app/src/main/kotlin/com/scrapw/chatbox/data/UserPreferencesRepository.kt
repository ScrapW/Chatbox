package com.scrapw.chatbox.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(val dataStore: DataStore<Preferences>) {
    private companion object {
        const val TAG = "UserPreferencesRepo"
        const val ERROR_READ = "Error reading preferences."

        val IP_ADDRESS = stringPreferencesKey("ip_address")
        val PORT = intPreferencesKey("port")

        val IS_REALTIME_MSG = booleanPreferencesKey("is_realtime_msg")
        val IS_TRIGGER_SFX = booleanPreferencesKey("is_trigger_sfx")
        val IS_SEND_IMMEDIATELY = booleanPreferencesKey("is_send_immediately")
    }

    val ipAddress = get(IP_ADDRESS, "127.0.0.1")

    suspend fun saveIpAddress(value: String) {
        save(IP_ADDRESS, value)
    }

    suspend fun savePort(value: Int) {
        save(PORT, value)
    }

    val isRealtimeMsg = get(IS_REALTIME_MSG, false)

    suspend fun saveIsRealtimeMsg(value: Boolean) {
        save(IS_REALTIME_MSG, value)
    }

    val isTriggerSfx = get(IS_TRIGGER_SFX, true)

    suspend fun saveIsTriggerSFX(value: Boolean) {
        save(IS_TRIGGER_SFX, value)
    }

    val isSendImmediately = get(IS_SEND_IMMEDIATELY, true)

    suspend fun saveIsSendImmediately(value: Boolean) {
        save(IS_SEND_IMMEDIATELY, value)
    }

    private fun <T> get(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
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

    private suspend fun <T> save(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}
