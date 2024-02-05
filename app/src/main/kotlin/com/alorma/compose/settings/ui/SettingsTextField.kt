package com.alorma.compose.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.alorma.compose.settings.storage.base.SettingValueState
import com.alorma.compose.settings.storage.base.rememberIntSettingState
import com.alorma.compose.settings.storage.base.rememberStringSettingState

@Composable
fun SettingsTextFieldString(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: SettingValueState<String?> = rememberStringSettingState(),
    title: String,
    icon: ImageVector? = null,
    useValueAsSubtitle: Boolean = true,
    subtitle: String? = null,
    action: (@Composable (Boolean) -> Unit)? = null,
    onValueChanged: ((String) -> Unit)? = null,
) {
    SettingsTextFieldString(
        modifier = modifier,
        enabled = enabled,
        state = state,
        icon = icon?.let {
            {
                Icon(
                    imageVector = icon,
                    contentDescription = null
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
        onValueChanged = onValueChanged
    )
}

@Composable
fun SettingsTextFieldInt(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: SettingValueState<Int> = rememberIntSettingState(),
    title: String,
    icon: ImageVector? = null,
    useValueAsSubtitle: Boolean = true,
    subtitle: String? = null,
    action: (@Composable (Boolean) -> Unit)? = null,
    onValueChanged: ((Int) -> Unit)? = null,
) {
    SettingsTextFieldInt(
        modifier = modifier,
        enabled = enabled,
        state = state,
        icon = icon?.let {
            {
                Icon(
                    imageVector = icon,
                    contentDescription = null
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
        onValueChanged = onValueChanged
    )
}

@Composable
fun SettingsTextFieldString(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: SettingValueState<String?> = rememberStringSettingState(),
    title: @Composable () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    useValueAsSubtitle: Boolean = true,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable (Boolean) -> Unit)? = null,
    onValueChanged: ((String) -> Unit)? = null,
) {

    var showDialog by remember { mutableStateOf(false) }

    val safeSubtitle = if (useValueAsSubtitle) {
        state.value?.let {
            { Text(text = it) }
        } ?: run {
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
                    value = inputState ?: "",
                    onValueChange = {
                        inputState = it
                    },
                    singleLine = true
                )
            }
        },
        onDismissRequest = { showDialog = false },
        confirmButton = {
            TextButton(
                onClick = {
                    state.value = inputState
                    showDialog = false

                    onValueChanged?.invoke(state.value ?: "")
                }
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
}

@Composable
fun SettingsTextFieldInt(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: SettingValueState<Int> = rememberIntSettingState(),
    title: @Composable () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    useValueAsSubtitle: Boolean = true,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable (Boolean) -> Unit)? = null,
    onValueChanged: ((Int) -> Unit)? = null,
) {

    var showDialog by remember { mutableStateOf(false) }

    val safeSubtitle = if (useValueAsSubtitle) {
        { Text(text = state.value.toString()) }
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
    var inputState by remember { mutableIntStateOf(state.value) }

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
                    value = inputState.toString(),
                    onValueChange = {
                        if (it.isDigitsOnly()) inputState = it.toInt()
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        onDismissRequest = { showDialog = false },
        confirmButton = {
            TextButton(
                onClick = {
                    state.value = inputState
                    showDialog = false
                    onValueChanged?.invoke(state.value)
                }
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

@Preview
@Composable
internal fun SettingsTextFieldIntPreview() {
    MaterialTheme {
        SettingsTextFieldInt(
            icon = { Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear") },
            title = { Text("SettingsTextFieldInt") }
        )
    }
}
