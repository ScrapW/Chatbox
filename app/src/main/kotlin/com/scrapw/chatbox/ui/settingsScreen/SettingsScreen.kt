package com.scrapw.chatbox.ui.settingsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LiveHelp
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSubGroup
import com.alorma.compose.settings.ui.SettingsSwitch
import com.alorma.compose.settings.ui.SettingsTextFieldInt
import com.alorma.compose.settings.ui.SettingsTextFieldString
import com.alorma.compose.settings.ui.SettingsUrl
import com.scrapw.chatbox.ChatboxScreen
import com.scrapw.chatbox.R
import com.scrapw.chatbox.UpdateStatus
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

        UpdateNotification(chatboxViewModel, navController)

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

        SettingsSubGroup("Floating button") {
            SettingsSwitch(
                state = SettingsStates.overlayState(),
                icon = Icons.Default.Layers,
                title = "Enable chatbox floating button",
                subtitle = "It can help to send Chatbox in VRChat Mobile."
            )


            SettingsSwitch(
                state = SettingsStates.overlayKeepOpen(),
                icon = Icons.Default.Lock,
                title = "Keep conversation overlay open",
                subtitle = "Overlay does not close after sending a message.",
                enabled = SettingsStates.overlayState().value
            )

            SettingsSwitch(
                state = SettingsStates.overlayLocalhost(),
                icon = Icons.Default.PhoneAndroid,
                title = "Send to localhost",
                subtitle = "Ignore the configured address, always send to this device.",
                enabled = SettingsStates.overlayState().value
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
//            SettingsSwitch(
//                state = SettingsStates.fullscreenState(),
//                icon = Icons.Default.Fullscreen,
//                title = stringResource(R.string.fullscreen),
//                subtitle = stringResource(R.string.fullscreen_desc)
//            )

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

@Composable
fun UpdateNotification(
    chatboxViewModel: ChatboxViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val updateInfo = chatboxViewModel.updateInfo
    if (chatboxViewModel.updateInfo.status == UpdateStatus.AVAILABLE && updateInfo.downloadUrl != null) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    navController.navigate(ChatboxScreen.About.name)
                },
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                Surface(
                    Modifier
                        .size(72.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(60.dp)
                ) {
                    Icon(
                        Icons.Default.Upgrade,
                        null,
                        Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        MaterialTheme.colorScheme.surfaceTint
                    )
                }

                Spacer(Modifier.width(8.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "New version available",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.surfaceTint
                    )
                    Text(
                        "${updateInfo.version}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.surfaceTint
                    )
                }
                Icon(
                    Icons.Default.ChevronRight,
                    null,
                    Modifier.padding(12.dp),
                    MaterialTheme.colorScheme.surfaceTint
                )
            }
        }
    }
}
