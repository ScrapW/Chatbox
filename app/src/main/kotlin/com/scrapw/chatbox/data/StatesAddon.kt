package com.scrapw.chatbox.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alorma.compose.settings.storage.base.SettingValueState
import com.alorma.compose.settings.storage.datastore.GenericPreferenceDataStoreSettingValueState
import com.alorma.compose.settings.storage.datastore.composeSettingsDataStore
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberPreferenceDataStoreStringSettingState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    dataStore: DataStore<Preferences> = LocalContext.current.composeSettingsDataStore,
    key: String,
    defaultValue: String = "",
): GenericPreferenceDataStoreSettingValueState<String> {
    return remember {
        GenericPreferenceDataStoreSettingValueState(
            coroutineScope = coroutineScope,
            dataStore = dataStore,
            dataStoreKey = stringPreferencesKey(key),
            defaultValue = defaultValue,
        )
    }
}

@Composable
fun rememberStringSettingState(defaultValue: String = ""): SettingValueState<String> {
    return remember { InMemoryStringSettingValueState(defaultValue) }
}

internal class InMemoryStringSettingValueState(private val defaultValue: String) :
    SettingValueState<String> {
    override var value: String by mutableStateOf(defaultValue)
    override fun reset() {
        value = defaultValue
    }
}