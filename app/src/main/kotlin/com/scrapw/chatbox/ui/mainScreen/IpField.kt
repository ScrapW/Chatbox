package com.scrapw.chatbox.ui.mainScreen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.scrapw.chatbox.R
import com.scrapw.chatbox.ui.ChatboxViewModel
import com.scrapw.chatbox.ui.MessengerUiState

@Composable
fun IpField(
    chatboxViewModel: ChatboxViewModel,
    uiState: MessengerUiState,
    modifier: Modifier = Modifier
) {

    var ipAddressChanged by remember { mutableStateOf(false) }

    val apply = {
        chatboxViewModel.ipAddressApply(uiState.ipAddress)
        chatboxViewModel.ipAddressLocked = true
        ipAddressChanged = false
    }


    Crossfade(
        targetState = chatboxViewModel.ipAddressLocked, label = "LockIpFieldCrossfade"
    ) { locked ->
        Row(
            modifier = modifier
                .height(72.dp)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = uiState.ipAddress,
                onValueChange = {
                    chatboxViewModel.onIpAddressChange(it)
                    ipAddressChanged = true
                },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp)),
                isError = !chatboxViewModel.isAddressResolvable.value,
                label = { Text(stringResource(R.string.ip_address)) },
                placeholder = { Text(stringResource(R.string.enter_your_ip_here)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        apply()
                    }
                ),
                enabled = !locked
            )

            if (!locked) {
                FilledTonalButton(
                    shape = MaterialTheme.shapes.medium,
                    onClick = {
                        apply()
                    },
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(64.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Apply IP Address."
                    )
                }
            } else {
                FilledTonalButton(
                    shape = MaterialTheme.shapes.medium,
                    onClick = {
                        chatboxViewModel.ipAddressLocked = false
                    },
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit IP Address."
                    )
                }
            }
        }

    }
}
