package com.scrapw.chatbox.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

val conversationUiState = ConversationUiState()

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    chatboxViewModel: ChatboxViewModel = viewModel(
        factory = ChatboxViewModel.Factory
    )
) {
    val uiState = chatboxViewModel.uiState.collectAsState().value

    Column(modifier) {
        Title()
        Surface(tonalElevation = 2.dp) {
            IpInputBox(chatboxViewModel, uiState)
        }
        Conversation(
            uiState = conversationUiState,
            onCopyPressed = chatboxViewModel::onMessageTextChange,
            modifier = Modifier.weight(1f)
        )
        Surface(
            tonalElevation = 2.dp,
            shape = MaterialTheme.shapes.large.copy(
                bottomEnd = CornerSize(0.dp),
                bottomStart = CornerSize(0.dp)
            )
        ) {
            Column(Modifier.padding(top = 10.dp)) {
                OptionList(chatboxViewModel, uiState, true)
                MessageInputBox(
                    chatboxViewModel,
                    uiState,
                    conversationUiState::addMessage
                )
            }
        }
    }
}
