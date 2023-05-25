package com.scrapw.chatbox.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

val conversationUiState = ConversationUiState()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    chatboxViewModel: ChatboxViewModel = viewModel(
        factory = ChatboxViewModel.Factory
    )
) {
    val uiState = chatboxViewModel.uiState.collectAsState().value
    Column() {
        TopAppBar(
            title = { Text("Chatbox") },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        IpInputBox(chatboxViewModel, uiState, modifier)
        Box(
            Modifier
                .weight(1f)
                .padding(12.dp)
                .clip(
                    RoundedCornerShape(8.dp)
                )
        ) {
            Crossfade(
                targetState = conversationUiState.messages.isEmpty(),
                animationSpec = tween(500), label = "MainScreenConversationCrossfade"
            ) { conversationEmpty ->
                if (conversationEmpty) {
                    EmptyConversationList(Modifier.fillMaxSize())
                } else {
                    ConversationList(
                        uiState = conversationUiState,
                        onCopyPressed = chatboxViewModel::onMessageTextChange,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        OptionList(chatboxViewModel, uiState, modifier)
        MessageInputBox(chatboxViewModel, uiState, conversationUiState::addMessage, modifier)
    }
}
