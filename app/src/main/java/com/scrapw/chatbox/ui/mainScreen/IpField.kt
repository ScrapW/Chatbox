package com.scrapw.chatbox.ui.mainScreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.scrapw.chatbox.ui.ChatboxViewModel
import com.scrapw.chatbox.ui.MessengerUiState

@Composable
fun IpField(
    chatboxViewModel: ChatboxViewModel,
    uiState: MessengerUiState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = uiState.ipAddress,
            onValueChange = {
                chatboxViewModel.onIpAddressChange(it)
            },
            modifier = Modifier
                .weight(1f)
                .padding(all = 8.dp)
                .clip(RoundedCornerShape(8.dp)),
            isError = !chatboxViewModel.isAddressResolvable.value,
            label = { Text("IP Address") },
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