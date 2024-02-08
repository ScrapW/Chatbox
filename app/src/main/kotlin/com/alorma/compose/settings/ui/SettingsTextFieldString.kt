package com.alorma.compose.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alorma.compose.settings.storage.base.SettingValueState
import com.scrapw.chatbox.data.rememberStringSettingState

@Composable
fun SettingsTextFieldString(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: SettingValueState<String> = rememberStringSettingState(),
    title: String,
    icon: ImageVector? = null,
    useValueAsSubtitle: Boolean = true,
    subtitle: String? = null,
    action: (@Composable (Boolean) -> Unit)? = null,
    submitOnDown: Boolean = true,
    onValueChanged: ((String) -> Unit)? = null,
    onSubmit: ((String) -> Unit)? = null
) {
    SettingsTextFieldString(
        modifier = modifier,
        enabled = enabled,
        state = state,
        icon = icon?.let {
            {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    Modifier.size(24.dp)
                )
            }
        },
        title = { Text(title) },
        subtitle = subtitle?.let {
            {
                Text(subtitle)
            }
        },
        useValueAsSubtitle = useValueAsSubtitle,
        action = action,
        submitOnDown = submitOnDown,
        onValueChanged = onValueChanged,
        onSubmit = onSubmit
    )
}

@Composable
fun SettingsTextFieldString(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: SettingValueState<String> = rememberStringSettingState(),
    title: @Composable () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    useValueAsSubtitle: Boolean = true,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable (Boolean) -> Unit)? = null,
    submitOnDown: Boolean = true,
    onValueChanged: ((String) -> Unit)? = null,
    onSubmit: ((String) -> Unit)? = null
) {

    var showDialog by remember { mutableStateOf(false) }

    val safeSubtitle = if (useValueAsSubtitle) {
//        state.value?.let {
//            { Text(text = it) }
//        } ?: run {
//            subtitle
//        }
        if (state.value != "") {
            { Text(text = state.value) }
        } else {
            subtitle
        }
    } else {
        subtitle
    }

    SettingsMenuLink(
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        title = title,
        subtitle = safeSubtitle,
        action = action,
        onClick = { showDialog = true },
    )

    if (!showDialog) return

    val scrollState = rememberScrollState()
    var inputState by remember { mutableStateOf(state.value) }
    val focusRequester = remember { FocusRequester() }

    val submit: () -> Unit = {
        state.value = inputState
        showDialog = false
        onSubmit?.invoke(state.value)
    }

    AlertDialog(
        title = title,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .selectableGroup(),
            ) {
                if (subtitle != null) {
                    subtitle()
                    Spacer(modifier = Modifier.size(8.dp))
                }
                TextField(
//                    value = inputState ?: "",
                    value = inputState,
                    onValueChange = {
                        inputState = it
                        onValueChanged?.invoke(inputState)
                    },
                    singleLine = true,
                    modifier = Modifier.focusRequester(focusRequester),
                    keyboardActions = KeyboardActions(
                        onDone = if (submitOnDown) {
                            {
                                submit()
                                showDialog = false
                            }
                        } else {
                            KeyboardActions.Default.onDone
                        }
                    )
                )
            }
        },
        onDismissRequest = { showDialog = false },
        confirmButton = {
            TextButton(
                onClick = submit
            ) {
                Text(
                    text = "OK"
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { showDialog = false }
            ) {
                Text(
                    text = "Cancel"
                )
            }
        }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}


@Preview
@Composable
internal fun SettingsTextFieldStringPreview() {
    MaterialTheme {
        SettingsTextFieldString(
            icon = { Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear") },
            title = { Text("SettingsTextFieldString") }
        )
    }
}