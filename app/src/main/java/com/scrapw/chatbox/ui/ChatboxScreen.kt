package com.scrapw.chatbox.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.scrapw.chatbox.ui.theme.ChatboxTheme

@Composable
fun IpInputBox(chatboxViewModel: ChatboxViewModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = chatboxViewModel.ipAddress.value,
            onValueChange = {
                chatboxViewModel.onIpAddressChange(it)
            },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Enter your ip here") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    chatboxViewModel.onIpAddressChange(chatboxViewModel.ipAddress.value)
                }
            )
        )
    }
}

@Composable
fun MessageInputBox(chatboxViewModel: ChatboxViewModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = chatboxViewModel.messageText.value,
            onValueChange = {
                chatboxViewModel.onMessageTextChange(it)
            },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Enter your message here") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    chatboxViewModel.sendMessage()
                }
            )
        )

        Button(
            onClick = {
                chatboxViewModel.sendMessage()
            },
            modifier = Modifier.size(48.dp),
            content = {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send Message",
                    tint = Color.White
                )
            }
        )
    }
}

@Composable
fun ChatScreen(chatboxViewModel: ChatboxViewModel = viewModel()) {
    Column() {
        IpInputBox(chatboxViewModel)
        MessageInputBox(chatboxViewModel)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    ChatboxTheme {
        ChatScreen()
    }
}
