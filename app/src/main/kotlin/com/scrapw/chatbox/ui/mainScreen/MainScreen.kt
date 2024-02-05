package com.scrapw.chatbox.ui.mainScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.scrapw.chatbox.data.SettingsStates
import com.scrapw.chatbox.ui.ChatboxViewModel
import com.scrapw.chatbox.ui.MessengerUiState


@Preview
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    chatboxViewModel: ChatboxViewModel = viewModel(
        factory = ChatboxViewModel.Factory
    ),
    uiState: MessengerUiState = MessengerUiState()
) {

    val displayIpState = SettingsStates.displayIpState()
    val displayMessageOptionsState = SettingsStates.displayMessageOptionsState()

    Column(modifier) {
        if (displayIpState.value) {
            Surface(tonalElevation = 2.dp) {
                IpField(
                    chatboxViewModel,
                    uiState
                )
            }
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
                if (displayMessageOptionsState.value) {
                    OptionList(chatboxViewModel, uiState, true)
                }
                MessageField(
                    chatboxViewModel
                )
            }
        }
    }
}
