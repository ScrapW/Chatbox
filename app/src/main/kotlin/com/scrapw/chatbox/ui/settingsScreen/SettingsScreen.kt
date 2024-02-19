package com.scrapw.chatbox.ui.settingsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LiveHelp
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSubGroup
import com.alorma.compose.settings.ui.SettingsSwitch
import com.alorma.compose.settings.ui.SettingsTextFieldInt
import com.alorma.compose.settings.ui.SettingsTextFieldString
import com.alorma.compose.settings.ui.SettingsUrl
import com.scrapw.chatbox.ChatboxScreen
import com.scrapw.chatbox.R
import com.scrapw.chatbox.data.SettingsStates
import com.scrapw.chatbox.ui.ChatboxViewModel


@Composable
fun SettingsScreen(
    chatboxViewModel: ChatboxViewModel,
    navController: NavController
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        // [Address]
        SettingsSubGroup(stringResource(R.string.osc_host)) {
            val ipAddressState = SettingsStates.ipAddress()
            SettingsTextFieldString(
                state = ipAddressState,
                title = stringResource(R.string.ip_address),
                onSubmit = {
                    chatboxViewModel.ipAddressApply(ipAddressState.value)
                }
            )

            val portState = SettingsStates.port()
            SettingsTextFieldInt(
                state = portState,
                defaultStateValue = 9000,
                title = stringResource(R.string.port),
                onSubmit = {
                    chatboxViewModel.portApply(portState.value)
                }
            )
        }

        SettingsSubGroup("Floating window") {
            SettingsSwitch(
                state = SettingsStates.floatingWindowState(),
                icon = Icons.Default.Cake,
                title = "com.scrapw.chatbox.overlay.Overlay",
                subtitle = ""
            )
        }

        // [Message]
        SettingsSubGroup(stringResource(R.string.message)) {
            SettingsSwitch(
                state = SettingsStates.messageRealtime(),
                icon = Icons.Default.FastForward,
                title = stringResource(R.string.real_time_sending),
                subtitle = stringResource(R.string.real_time_sending_desc)
            )

            SettingsSwitch(
                state = SettingsStates.messageTriggerSfx(),
                icon = Icons.Default.NotificationsActive,
                title = stringResource(R.string.trigger_notification_sound),
                subtitle = stringResource(R.string.trigger_notification_sound_desc)
            )

            SettingsSwitch(
                state = SettingsStates.messageTypingIndicator(),
                icon = ImageVector.vectorResource(R.drawable.indicator),
                title = stringResource(R.string.show_message_typing_indicator),
                subtitle = stringResource(R.string.show_message_typing_indicator_desc)
            )

            SettingsSwitch(
                state = SettingsStates.messageSendDirectly(),
                icon = Icons.AutoMirrored.Default.Send,
                title = stringResource(R.string.send_message_directly),
                subtitle = stringResource(R.string.send_message_directly_desc)
            )
        }

        // [Layout]
        SettingsSubGroup(stringResource(R.string.layout)) {
            // Display IP field
            SettingsSwitch(
                state = SettingsStates.displayIpState(),
                title = stringResource(R.string.display_ip_edit_bar)
            )

            // Display message options
            SettingsSwitch(
                state = SettingsStates.displayMessageOptionsState(),
                title = stringResource(R.string.display_message_options)
            )
        }

        // [Accessibility]
        SettingsSubGroup(stringResource(R.string.accessibility)) {
            // Fullscreen
            SettingsSwitch(
                state = SettingsStates.fullscreenState(),
                icon = Icons.Default.Fullscreen,
                title = stringResource(R.string.fullscreen),
                subtitle = stringResource(R.string.fullscreen_desc)
            )

//            // Always show keyboard
//            SettingsSwitch(
//                state = SettingsStates.alwaysShowKeyboardState(),
//                icon = Icons.Default.Keyboard,
//                title = stringResource(R.string.always_show_keyboard),
//                subtitle = stringResource(R.string.always_show_keyboard_desc)
//            )

            // Button haptic
            SettingsSwitch(
                state = SettingsStates.buttonHapticState(),
                icon = Icons.Default.Vibration,
                title = stringResource(R.string.button_haptic),
                subtitle = stringResource(R.string.button_haptic_desc)
            )
        }

        SettingsSubGroup(stringResource(R.string.information)) {
            SettingsMenuLink(
                icon = Icons.Default.Info,
                title = stringResource(R.string.about)
            ) {
                navController.navigate(ChatboxScreen.About.name)
            }
            SettingsUrl(
                icon = Icons.AutoMirrored.Default.LiveHelp,
                title = stringResource(R.string.faq),
                url = "https://github.com/ScrapW/Chatbox",
                useUrlAsSubtitle = false
            )
        }
    }
}