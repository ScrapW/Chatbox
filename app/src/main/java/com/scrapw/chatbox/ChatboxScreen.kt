package com.scrapw.chatbox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.scrapw.chatbox.ui.ChatboxViewModel
import com.scrapw.chatbox.ui.Conversation
import com.scrapw.chatbox.ui.IpField
import com.scrapw.chatbox.ui.MessageField
import com.scrapw.chatbox.ui.OptionList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatboxAppBar() {
    TopAppBar(
        title = {
            Row {
                Text("Chatbox")
            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}


@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    chatboxViewModel: ChatboxViewModel = viewModel(
        factory = ChatboxViewModel.Factory
    )
) {
    val messengerUiState by chatboxViewModel.messengerUiState.collectAsState()

    Column(modifier) {
        ChatboxAppBar()
        Surface(tonalElevation = 2.dp) {
            IpField(
                chatboxViewModel,
                messengerUiState
            )
        }
        Conversation(
            uiState = chatboxViewModel.conversationUiState,
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
                OptionList(chatboxViewModel, messengerUiState, true)
                MessageField(
                    chatboxViewModel
                )
            }
        }
    }
}
