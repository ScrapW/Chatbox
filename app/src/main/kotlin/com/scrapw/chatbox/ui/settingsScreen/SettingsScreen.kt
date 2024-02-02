package com.scrapw.chatbox.ui.settingsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alorma.compose.settings.storage.datastore.rememberPreferenceDataStoreBooleanSettingState
import com.alorma.compose.settings.ui.SettingsSwitch
import com.scrapw.chatbox.ui.ChatboxViewModel


@Composable
fun SettingsScreen(
    chatboxViewModel: ChatboxViewModel
) {
    Column(Modifier.fillMaxSize()) {

        // [Layout]
        // Display IP field
        val displayIpState =
            rememberPreferenceDataStoreBooleanSettingState(
                key = "display_ip",
                defaultValue = true,
                dataStore = chatboxViewModel.dataStore
            )

        SettingsSwitch(
            state = displayIpState,
            title = { Text(text = "Display IP edit bar") }
        )


        // Display message options
        val displayMessageOptionsState =
            rememberPreferenceDataStoreBooleanSettingState(
                key = "display_msg_options",
                defaultValue = true,
                dataStore = chatboxViewModel.dataStore
            )

        SettingsSwitch(
            state = displayMessageOptionsState,
            title = { Text(text = "Display message options") }
        )


        // [Accessibility]

        // Fullscreen
        val fullscreenState =
            rememberPreferenceDataStoreBooleanSettingState(
                key = "fullscreen",
                defaultValue = false,
                dataStore = chatboxViewModel.dataStore
            )

        SettingsSwitch(
            icon = {
                Icon(
                    imageVector = Icons.Default.Fullscreen,
                    contentDescription = "Fullscreen"
                )
            },
            state = fullscreenState,
            title = { Text(text = "Fullscreen") },
            subtitle = { Text(text = "Fullscreen message screen.") }
        )


        // Always show keyboard
        val alwaysShowKeyboardState =
            rememberPreferenceDataStoreBooleanSettingState(
                key = "always_show_keyboard",
                defaultValue = false,
                dataStore = chatboxViewModel.dataStore
            )

        SettingsSwitch(
            icon = { Icon(imageVector = Icons.Default.Keyboard, contentDescription = "Keyboard") },
            state = alwaysShowKeyboardState,
            title = { Text(text = "Always show keyboard") },
            subtitle = { Text(text = "Keep your keyboard pop up when in message screen.") }
        )


        // Send button vibration
        val sendButtonVibrationState =
            rememberPreferenceDataStoreBooleanSettingState(
                key = "vibration_when_send",
                defaultValue = true,
                dataStore = chatboxViewModel.dataStore
            )

        SettingsSwitch(
            icon = {
                Icon(
                    imageVector = Icons.Default.Vibration,
                    contentDescription = "Vibration"
                )
            },
            state = sendButtonVibrationState,
            title = { Text(text = "Send button vibration") },
            subtitle = { Text(text = "Vibrates when you click the send button.") }
        )
    }
}