package com.scrapw.chatbox.overlay

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun ButtonOverlay(onExpand: () -> Unit) {
    FloatingActionButton(
        onClick = { onExpand() }
    ) {
        Icon(Icons.Default.Cake, null)
    }
}

@Composable
fun MessengerOverlay(onExpand: () -> Unit) {

    val focusRequester = remember { FocusRequester() }

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onPress = { Log.d("Overlay gesture", "onPress()") },
                onDoubleTap = { Log.d("Overlay gesture", "onDoubleTap()") },
                onLongPress = { Log.d("Overlay gesture", "onLongPress()") },
                onTap = { Log.d("Overlay gesture", "onTap()") }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize()
        ) {
            Text(text = "Hello from Compose")

            var text by remember { mutableStateOf("Hello") }

            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Label") },
                modifier = Modifier.focusRequester(focusRequester)
            )
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}