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

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private companion object {
        const val TAG = "UserPreferencesRepo"
        const val ERROR_READ = "Error reading preferences."

        val IP_ADDRESS = stringPreferencesKey("ip_address")
        val PORT = intPreferencesKey("port")

        val MSG_REALTIME = booleanPreferencesKey("msg_realtime")
        val MSG_TRIGGER_SFX = booleanPreferencesKey("msg_trigger_sfx")
        val MSG_TYPING_INDICATOR = booleanPreferencesKey("msg_typing_indicator")
        val MSG_SEND_DIRECTLY = booleanPreferencesKey("msg_send_directly")
    }

    val ipAddress = get(IP_ADDRESS, "127.0.0.1")

    suspend fun saveIpAddress(value: String) {
        save(IP_ADDRESS, value)
    }

    suspend fun savePort(value: Int) {
        save(PORT, value)
    }

    val isRealtimeMsg = get(MSG_REALTIME, false)

    suspend fun saveIsRealtimeMsg(value: Boolean) {
        save(MSG_REALTIME, value)
    }

    val isTriggerSfx = get(MSG_TRIGGER_SFX, true)

    suspend fun saveIsTriggerSFX(value: Boolean) {
        save(MSG_TRIGGER_SFX, value)
    }

    val isTypingIndicator = get(MSG_TYPING_INDICATOR, true)
    suspend fun saveTypingIndicator(value: Boolean) {
        save(MSG_TYPING_INDICATOR, value)
    }

    val isSendImmediately = get(MSG_SEND_DIRECTLY, true)

    suspend fun saveIsSendImmediately(value: Boolean) {
        save(MSG_SEND_DIRECTLY, value)
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
