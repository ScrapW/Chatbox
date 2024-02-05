package com.alorma.compose.settings.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alorma.compose.settings.storage.base.rememberBooleanSettingState
import com.alorma.compose.settings.storage.base.rememberFloatSettingState

@Preview
@Composable
internal fun SettingsPreview() {
    MaterialTheme {
        val storage = rememberBooleanSettingState(defaultValue = true)
        var rememberCheckBoxState by remember { mutableStateOf(true) }

        Column(
            Modifier.verticalScroll(rememberScrollState())
        ) {
            SettingsCheckbox(
                state = storage,
                icon = { Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear") },
                title = { Text(text = "Settings Checkbox") },
                subtitle = { Text(text = "This is a longer text") },
                onCheckedChange = { },
            )

            SettingsGroup(
                title = { Text(text = "Settings group title") },
            ) {
                Box(
                    modifier = Modifier
                        .height(64.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Settings group")
                }
            }

            SettingsList(
                items = listOf("Banana", "Kiwi", "Pineapple"),
                icon = { Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear") },
                title = { Text(text = "Settings List") },
                subtitle = { Text(text = "This is a longer text") },
            )


//            Crash

//            SettingsListDropdown(
//                items = listOf("Banana", "Kiwi", "Pineapple"),
//                icon = { Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear") },
//                title = { Text(text = "Settings List Dropdown") },
//                subtitle = { Text(text = "This is a longer text") },
//            )

            SettingsListMultiSelect(
                items = listOf("Banana", "Kiwi", "Pineapple"),
                icon = { Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear") },
                title = { Text(text = "Settings List Multi Select") },
                subtitle = { Text(text = "This is a longer text") },
                confirmButton = "Confirm"
            )

            SettingsMenuLink(
                icon = { Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear") },
                title = { Text(text = "Settings Menu Link") },
                subtitle = { Text(text = "This is a longer text") },
                action = {
                    Checkbox(checked = rememberCheckBoxState, onCheckedChange = { newState ->
                        rememberCheckBoxState = newState
                    })
                },
            ) {}

            SettingsSlider(
                state = rememberFloatSettingState(),
                title = { Text(text = "Settings Slider") }
            )

            SettingsTextFieldString(
                title = { Text("SettingsTextFieldString") }
            )

            SettingsTextFieldInt(
                title = { Text("SettingsTextFieldInt") }
            )

            SettingsSubGroup(title = { Text(text = "Settings sub group title") }) {
                SettingsSwitch(
                    state = storage,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear"
                        )
                    },
                    title = { Text(text = "Settings Switch") },
                    subtitle = { Text(text = "This is a longer text") },
                    onCheckedChange = { },
                )

                SettingsSwitch(
                    state = storage,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear"
                        )
                    },
                    title = { Text(text = "Settings Switch") },
                    onCheckedChange = { },
                )

                SettingsSwitch(
                    state = storage,
                    title = { Text(text = "Settings Switch") },
                    subtitle = { Text(text = "This is a longer text") },
                    onCheckedChange = { },
                )

                SettingsSwitch(
                    state = storage,
                    title = { Text(text = "Settings Switch") },
                    onCheckedChange = { },
                )
            }
        }
    }
}
