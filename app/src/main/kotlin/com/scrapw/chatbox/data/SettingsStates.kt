package com.scrapw.chatbox.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.alorma.compose.settings.storage.datastore.GenericPreferenceDataStoreSettingValueState
import com.alorma.compose.settings.storage.datastore.rememberPreferenceDataStoreBooleanSettingState
import com.alorma.compose.settings.storage.datastore.rememberPreferenceDataStoreIntSettingState
import com.scrapw.chatbox.dataStore

object SettingsStates {

    @Composable
    fun ipAddress(): GenericPreferenceDataStoreSettingValueState<String> {
        return rememberPreferenceDataStoreStringSettingState(
            key = "ip_address",
            defaultValue = "127.0.0.1",
            dataStore = LocalContext.current.dataStore
        )
    }

    @Composable
    fun port(): GenericPreferenceDataStoreSettingValueState<Int> {
        return rememberPreferenceDataStoreIntSettingState(
            key = "port",
            defaultValue = 9000,
            dataStore = LocalContext.current.dataStore
        )
    }

    @Composable
    fun overlayState(): GenericPreferenceDataStoreSettingValueState<Boolean> {
        return rememberPreferenceDataStoreBooleanSettingState(
            key = "overlay",
            defaultValue = false,
            dataStore = LocalContext.current.dataStore
        )
    }

    @Composable
    fun overlayKeepOpen(): GenericPreferenceDataStoreSettingValueState<Boolean> {
        return rememberPreferenceDataStoreBooleanSettingState(
            key = "overlay_keep_open",
            defaultValue = true,
            dataStore = LocalContext.current.dataStore
        )
    }

    @Composable
    fun overlayLocalhost(): GenericPreferenceDataStoreSettingValueState<Boolean> {
        return rememberPreferenceDataStoreBooleanSettingState(
            key = "overlay_localhost",
            defaultValue = true,
            dataStore = LocalContext.current.dataStore
        )
    }

    @Composable
    fun messageRealtime(): GenericPreferenceDataStoreSettingValueState<Boolean> {
        return rememberPreferenceDataStoreBooleanSettingState(
            key = "msg_realtime",
            defaultValue = false,
            dataStore = LocalContext.current.dataStore
        )
    }

    @Composable
    fun messageTriggerSfx(): GenericPreferenceDataStoreSettingValueState<Boolean> {
        return rememberPreferenceDataStoreBooleanSettingState(
            key = "msg_trigger_sfx",
            defaultValue = true,
            dataStore = LocalContext.current.dataStore
        )
    }

    @Composable
    fun messageTypingIndicator(): GenericPreferenceDataStoreSettingValueState<Boolean> {
        return rememberPreferenceDataStoreBooleanSettingState(
            key = "msg_typing_indicator",
            defaultValue = true,
            dataStore = LocalContext.current.dataStore
        )
    }

    @Composable
    fun messageSendDirectly(): GenericPreferenceDataStoreSettingValueState<Boolean> {
        return rememberPreferenceDataStoreBooleanSettingState(
            key = "msg_send_directly",
            defaultValue = true,
            dataStore = LocalContext.current.dataStore
        )
    }

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