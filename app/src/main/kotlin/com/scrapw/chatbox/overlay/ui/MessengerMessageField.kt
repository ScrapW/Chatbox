package com.scrapw.chatbox.overlay.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.scrapw.chatbox.R
import com.scrapw.chatbox.data.SettingsStates
import com.scrapw.chatbox.ui.ChatboxViewModel
import com.scrapw.chatbox.ui.common.HapticConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun MessengerMessageField(chatboxViewModel: ChatboxViewModel, collapse: () -> Unit) {
    val focusRequester = remember { FocusRequester() }

    val keepOpenState = SettingsStates.overlayKeepOpen()

    val localhostState = SettingsStates.overlayLocalhost()

    val onSend = {
        chatboxViewModel.sendMessage(localhostState.value)
        if (!keepOpenState.value) {
            collapse()
        }
    }

    val onStash = {
        chatboxViewModel.stashMessage(localhostState.value)
        if (!keepOpenState.value) {
            collapse()
        }
    }

    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    keepOpenState.value = !keepOpenState.value
                },
        ) {
            Crossfade(
                targetState = keepOpenState.value,
                animationSpec = tween(500), label = "OverlayLockCrossfade"
            ) { locked ->
                val icon = if (locked) Icons.Default.Lock else Icons.Default.LockOpen
                val contentDescription =
                    if (locked) stringResource(R.string.overlay_locked)
                    else stringResource(R.string.overlay_unlocked)
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        TextField(
            value = chatboxViewModel.messageText.value,
            onValueChange = {
                chatboxViewModel.onMessageTextChange(it, localhostState.value)
            },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .onKeyEvent { event ->
                    if (event.key == Key.Back) {
                        collapse()
                    }
                    true
                },
            singleLine = true,
            placeholder = { Text(stringResource(R.string.write_a_message)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    onSend()
                }
            )
        )

        val interactionSource = remember { MutableInteractionSource() }

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxHeight()
                .scale(0.9f),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(16.dp),
            interactionSource = interactionSource
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.Send,
                contentDescription = stringResource(R.string.send),
                modifier = Modifier.size(24.dp)
            )
        }

        val buttonHapticState = SettingsStates.buttonHapticState()
        val view = LocalView.current

        val onSendClick = {
            onSend()
            if (buttonHapticState.value) {
                view.performHapticFeedback(HapticConstants.send)
            }
        }

        val onSendLongClick = {
            onStash()
            if (buttonHapticState.value) {
                view.performHapticFeedback(HapticConstants.send)
            }
        }

        val viewConfiguration = LocalViewConfiguration.current

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
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}