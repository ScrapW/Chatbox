package com.scrapw.chatbox.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

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
