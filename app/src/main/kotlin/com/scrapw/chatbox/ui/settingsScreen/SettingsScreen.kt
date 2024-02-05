package com.scrapw.chatbox.ui.settingsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alorma.compose.settings.ui.SettingsSwitch
import com.scrapw.chatbox.data.SettingsStates
import com.scrapw.chatbox.ui.ChatboxViewModel
import com.scrapw.chatbox.ui.SettingsSubtitle


@Composable
fun SettingsScreen(
    chatboxViewModel: ChatboxViewModel
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        // [Layout]
        SettingsSubtitle(text = "Layout")

        // Display IP field
        SettingsSwitch(
            state = SettingsStates.displayIpState(),
            title = { Text(text = "Display IP edit bar") }
        )

        // Display message options
        SettingsSwitch(
            state = SettingsStates.displayMessageOptionsState(),
            title = { Text(text = "Display message options") }
        )

        // [Accessibility]

        // Fullscreen
        SettingsSwitch(
            icon = {
                Icon(
                    imageVector = Icons.Default.Fullscreen,
                    contentDescription = "Fullscreen"
                )
            },
            state = SettingsStates.fullscreenState(),
            title = { Text(text = "Fullscreen") },
            subtitle = { Text(text = "Fullscreen message screen.") }
        )

        // Always show keyboard
        SettingsSwitch(
            icon = { Icon(imageVector = Icons.Default.Keyboard, contentDescription = "Keyboard") },
            state = SettingsStates.alwaysShowKeyboardState(),
            title = { Text(text = "Always show keyboard") },
            subtitle = { Text(text = "Keep your keyboard pop up when in message screen.") }
        )

        // Button haptic
        SettingsSwitch(
            icon = { Icon(imageVector = Icons.Default.Vibration, contentDescription = "Haptic") },
            state = SettingsStates.buttonHapticState(),
            title = { Text(text = "Button haptic") },
            subtitle = { Text(text = "Provide haptic feedback when touch buttons in message screen.") }
        )
    }
}