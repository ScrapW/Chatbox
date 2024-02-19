package com.scrapw.chatbox.overlay.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.scrapw.chatbox.R

@Composable
fun ButtonOverlay(expand: () -> Unit) {
    FloatingActionButton(
        containerColor = FloatingActionButtonDefaults.containerColor.copy(alpha = 0.8f),
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
        onClick = { expand() }
    ) {
        Icon(
            Icons.AutoMirrored.Outlined.Chat,
            stringResource(R.string.open_chatbox),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}