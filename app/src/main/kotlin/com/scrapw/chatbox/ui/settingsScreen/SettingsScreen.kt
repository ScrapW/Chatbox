package com.scrapw.chatbox.ui.settingsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alorma.compose.settings.ui.SettingsSubGroup
import com.alorma.compose.settings.ui.SettingsSwitch
import com.alorma.compose.settings.ui.SettingsTextFieldInt
import com.alorma.compose.settings.ui.SettingsTextFieldString
import com.scrapw.chatbox.data.SettingsStates
import com.scrapw.chatbox.ui.ChatboxViewModel


@Composable
fun SettingsScreen(
    chatboxViewModel: ChatboxViewModel
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        // [Address]
        SettingsSubGroup("OSC Host") {
            val ipAddressState = SettingsStates.ipAddress()
            SettingsTextFieldString(
                state = ipAddressState,
                title = "IP Address",
                onSubmit = {
                    chatboxViewModel.ipAddressApply(ipAddressState.value)
                }
            )

            val portState = SettingsStates.port()
            SettingsTextFieldInt(
                state = portState,
                title = "Port",
                onSubmit = {
                    chatboxViewModel.portApply(portState.value)
                }
            )
        }

        // [Layout]
        SettingsSubGroup("Layout") {
            // Display IP field
            SettingsSwitch(
                state = SettingsStates.displayIpState(),
                title = "Display IP edit bar"
            )

            // Display message options
            SettingsSwitch(
                state = SettingsStates.displayMessageOptionsState(),
                title = "Display message options"
            )
        }

        SettingsSubGroup("Accessibility") {
            // [Accessibility]

            // Fullscreen
            SettingsSwitch(
                state = SettingsStates.fullscreenState(),
                icon = Icons.Default.Fullscreen,
                title = "Fullscreen",
                subtitle = "Fullscreen message screen."
            )

//            // Always show keyboard
//            SettingsSwitch(
//                state = SettingsStates.alwaysShowKeyboardState(),
//                icon = Icons.Default.Keyboard,
//                title = "Always show keyboard",
//                subtitle = "Keep your keyboard pop up when in message screen."
//            )

            // Button haptic
            SettingsSwitch(
                state = SettingsStates.buttonHapticState(),
                icon = Icons.Default.Vibration,
                title = "Button haptic",
                subtitle = "Provide haptic feedback when touch buttons in message screen."
            )
        }
    }
}