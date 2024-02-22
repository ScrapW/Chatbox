package com.scrapw.chatbox.ui.mainScreen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.scrapw.chatbox.R
import com.scrapw.chatbox.data.SettingsStates
import com.scrapw.chatbox.ui.ChatboxViewModel
import com.scrapw.chatbox.ui.common.HapticConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest


@Composable
fun MessageField(
    chatboxViewModel: ChatboxViewModel,
    modifier: Modifier = Modifier
) {
    Crossfade(
        targetState = chatboxViewModel.isAddressResolvable.value, label = "LockIpFieldCrossfade"
    ) { resolvable ->
        Row(
            modifier = modifier
                .height(72.dp)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            if (resolvable) {
                TextField(
                    value = chatboxViewModel.messageText.value,
                    onValueChange = {
                        chatboxViewModel.onMessageTextChange(it)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp)),
                    singleLine = true,
                    placeholder = { Text(stringResource(R.string.write_a_message)) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            chatboxViewModel.sendMessage()
                        }
                    )
                )
            } else {
                TextField(
                    value = stringResource(R.string.invalid_ip_address),
                    onValueChange = {},
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp)),
                    textStyle = TextStyle.Default.copy(
                        fontStyle = FontStyle.Italic
                    ),
//                    colors = TextFieldDefaults.colors(
//                        disabledTextColor = MaterialTheme.colorScheme.error
//                    ),
                    enabled = false
                )
            }


            val view = LocalView.current
            val buttonHapticState = SettingsStates.buttonHapticState()


            val onSendClick = {
                chatboxViewModel.ipAddressLocked = true
                chatboxViewModel.sendMessage()
                if (buttonHapticState.value) {
                    view.performHapticFeedback(HapticConstants.send)
                }
            }

            val onSendLongClick = {
                chatboxViewModel.ipAddressLocked = true
                chatboxViewModel.stashMessage()
                if (buttonHapticState.value) {
                    view.performHapticFeedback(HapticConstants.send)
                }
            }


            val interactionSource = remember { MutableInteractionSource() }
            val viewConfiguration = LocalViewConfiguration.current

            Button(
                onClick = {},
                modifier = Modifier.fillMaxHeight(),
                interactionSource = interactionSource,
                enabled = resolvable
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Send,
                    contentDescription = stringResource(R.string.send),
                    modifier = Modifier.size(24.dp)
                )
            }

            LaunchedEffect(interactionSource) {
                var isLongClick = false

                interactionSource.interactions.collectLatest { interaction ->
                    when (interaction) {
                        is PressInteraction.Press -> {
                            isLongClick = false
                            delay(viewConfiguration.longPressTimeoutMillis)
                            isLongClick = true
                            onSendLongClick()
                        }

                        is PressInteraction.Release -> {
                            if (!isLongClick) {
                                onSendClick()
                            }
                        }
                    }
                }
            }
        }
    }
}