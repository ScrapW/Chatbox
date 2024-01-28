package com.scrapw.chatbox.ui

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
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import java.time.Instant

fun sendMessage(
    chatboxViewModel: ChatboxViewModel,
    addMessage: (Message) -> Unit
) {
    addMessage(
        Message(chatboxViewModel.messageText.value.text, Instant.now())

    )
    chatboxViewModel.sendMessage()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInputBox(
    chatboxViewModel: ChatboxViewModel,
    uiState: ChatboxUiState,
    addMessage: (Message) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(72.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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
            placeholder = { Text("Write a message...") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    sendMessage(chatboxViewModel, addMessage)
                }
            )
        )

        Button(
            modifier = Modifier.fillMaxHeight(),
            onClick = {
                sendMessage(chatboxViewModel, addMessage)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}