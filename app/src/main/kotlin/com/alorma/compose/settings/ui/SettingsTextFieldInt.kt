package com.alorma.compose.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alorma.compose.settings.storage.base.SettingValueState
import com.alorma.compose.settings.storage.base.rememberIntSettingState


@Composable
fun SettingsTextFieldInt(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: SettingValueState<Int> = rememberIntSettingState(),
    defaultStateValue: Int,
    title: String,
    icon: ImageVector? = null,
    useValueAsSubtitle: Boolean = true,
    subtitle: String? = null,
    action: (@Composable (Boolean) -> Unit)? = null,
    submitOnDown: Boolean = true,
    onValueChanged: ((Int) -> Unit)? = null,
    onSubmit: ((Int) -> Unit)? = null
) {
    SettingsTextFieldInt(
        modifier = modifier,
        enabled = enabled,
        state = state,
        defaultStateValue = defaultStateValue,
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
fun SettingsTextFieldInt(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: SettingValueState<Int> = rememberIntSettingState(),
    defaultStateValue: Int,
    title: @Composable () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    useValueAsSubtitle: Boolean = true,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable (Boolean) -> Unit)? = null,
    submitOnDown: Boolean = true,
    onValueChanged: ((Int) -> Unit)? = null,
    onSubmit: ((Int) -> Unit)? = null
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
    var inputState by remember { mutableStateOf(TextFieldValue(state.value.toString())) }
    val focusRequester = remember { FocusRequester() }

    val submit: () -> Unit = {
        try {
            state.value = inputState.text.toInt()
        } catch (e: Exception) {
            state.value = defaultStateValue
        }
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
                    value = inputState,
                    onValueChange = {
                        inputState = it
                        try {
                            onValueChanged?.invoke(inputState.text.toInt())
                        } catch (e: Exception) {
                            // Failed toInt(), do nothing.
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
        inputState = inputState.copy(selection = TextRange(inputState.text.length))
        focusRequester.requestFocus()
    }
}

@Preview
@Composable
internal fun SettingsTextFieldIntPreview() {
    MaterialTheme {
        SettingsTextFieldInt(
            defaultStateValue = -1,
            icon = { Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear") },
            title = { Text("SettingsTextFieldInt") }
        )
    }
}
