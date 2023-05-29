package com.scrapw.chatbox.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    chatboxViewModel: ChatboxViewModel = viewModel(
        factory = ChatboxViewModel.Factory
    )
) {
    val uiState = chatboxViewModel.uiState.collectAsState().value
    val conversationUiState by remember { mutableStateOf(ConversationUiState()) }

    Column() {
        Title()
        IpInputBox(chatboxViewModel, uiState, modifier)
        Conversation(
            uiState = conversationUiState,
            onCopyPressed = chatboxViewModel::onMessageTextChange,
            modifier = Modifier.weight(1f)
        )
        OptionList(chatboxViewModel, uiState, true, modifier)
        MessageInputBox(chatboxViewModel, uiState, conversationUiState::addMessage, modifier)
    }
}
