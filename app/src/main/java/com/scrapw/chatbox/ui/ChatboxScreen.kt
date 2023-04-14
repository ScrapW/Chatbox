package com.scrapw.chatbox.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun IpInputBox(
    chatboxViewModel: ChatboxViewModel,
    uiState: ChatboxUiState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = chatboxViewModel.ipAddressText.value,
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
                    chatboxViewModel.ipAddressApply()
                }
            )
        )
    }
}

@Composable
fun MessageInputBox(
    chatboxViewModel: ChatboxViewModel,
    uiState: ChatboxUiState,
    modifier: Modifier = Modifier
) {
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
fun Option(
    description: String,
    isChecked: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(
        Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onChange,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(description)
    }
}

@Composable
fun OptionList(
    chatboxViewModel: ChatboxViewModel,
    uiState: ChatboxUiState,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Option(
            "Real-time Message",
            uiState.isRealtimeMsg,
            chatboxViewModel::onRealtimeMsgChanged
        )
        Option(
            "Trigger Notification SFX",
            uiState.isTriggerSFX,
            chatboxViewModel::onTriggerSfxChanged
        )
        Option(
            "Send Message Immediately",
            uiState.isSendImmediately,
            chatboxViewModel::onSendImmediatelyChanged
        )
    }
}

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    chatboxViewModel: ChatboxViewModel = viewModel(
        factory = ChatboxViewModel.Factory
    )
) {
    val uiState = chatboxViewModel.uiState.collectAsState().value
    Column() {
        IpInputBox(chatboxViewModel, uiState, modifier)
        MessageInputBox(chatboxViewModel, uiState, modifier)
        OptionList(chatboxViewModel, uiState, modifier)
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun DefaultPreview() {
//    ChatboxTheme {
//        ChatScreen()
//    }
//}
