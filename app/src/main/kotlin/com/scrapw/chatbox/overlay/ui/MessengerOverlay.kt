package com.scrapw.chatbox.overlay.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.scrapw.chatbox.ui.ChatboxViewModel


@Composable
fun MessengerOverlay(chatboxViewModel: ChatboxViewModel, collapse: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp).copy(alpha = 0.9f),
        modifier = Modifier.clip(RoundedCornerShape(4.dp))
    ) {
        Column {
            MessengerMessageField(chatboxViewModel, collapse)
            MessengerConversation(
                uiState = chatboxViewModel.conversationUiState,
                onCopyPressed = chatboxViewModel::onMessageTextChange,
                modifier = Modifier.fillMaxHeight(0.5f)
            )
        }
    }
}



