package com.scrapw.chatbox.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.alorma.compose.settings.storage.datastore.GenericPreferenceDataStoreSettingValueState
import com.alorma.compose.settings.storage.datastore.rememberPreferenceDataStoreBooleanSettingState
import com.scrapw.chatbox.dataStore

object SettingsStates {
    @Composable
    fun displayIpState(): GenericPreferenceDataStoreSettingValueState<Boolean> {
        return rememberPreferenceDataStoreBooleanSettingState(
            key = "display_ip",
            defaultValue = true,
            dataStore = LocalContext.current.dataStore
        )
    }

    @Composable
    fun displayMessageOptionsState(): GenericPreferenceDataStoreSettingValueState<Boolean> {
        return rememberPreferenceDataStoreBooleanSettingState(
            key = "display_msg_options",
            defaultValue = true,
            dataStore = LocalContext.current.dataStore
        )
    }

    @Composable
    fun fullscreenState(): GenericPreferenceDataStoreSettingValueState<Boolean> {
        return rememberPreferenceDataStoreBooleanSettingState(
            key = "fullscreen",
            defaultValue = false,
            dataStore = LocalContext.current.dataStore
        )
    }

    @Composable
    fun alwaysShowKeyboardState(): GenericPreferenceDataStoreSettingValueState<Boolean> {
        return rememberPreferenceDataStoreBooleanSettingState(
            key = "always_show_keyboard",
            defaultValue = false,
            dataStore = LocalContext.current.dataStore
        )
    }

    @Composable
    fun buttonHapticState(): GenericPreferenceDataStoreSettingValueState<Boolean> {
        return rememberPreferenceDataStoreBooleanSettingState(
            key = "button_haptic",
            defaultValue = true,
            dataStore = LocalContext.current.dataStore
        )
    }
}