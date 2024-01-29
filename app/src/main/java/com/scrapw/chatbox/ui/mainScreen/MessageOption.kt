package com.scrapw.chatbox.ui.mainScreen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.scrapw.chatbox.ui.ChatboxViewModel
import com.scrapw.chatbox.ui.MessengerUiState

private data class Option(
    val description: String,
    val shortDescription: String,
    val icon: ImageVector,
    val isChecked: Boolean,
    val onChange: (Boolean) -> Unit = {}
)

@Composable
fun ChipOption(
    description: String,
    icon: ImageVector,
    isChecked: Boolean,
    onChange: (Boolean) -> Unit,
) {
    val iconColor =
        if (isChecked) MaterialTheme.colorScheme.primary
        else Color.Gray

    Crossfade(
        targetState = Pair(isChecked, iconColor),
        animationSpec = tween(500), label = ""
    ) { (isChecked, iconColor) ->
        FilterChip(
            selected = isChecked,
            onClick = { onChange(!isChecked) },
            label = { Text(description) },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            },
            modifier = Modifier.height(36.dp)
        )
    }
}

@Composable
fun BarOption(
    description: String,
    icon: ImageVector,
    isChecked: Boolean,
    onChange: (Boolean) -> Unit,
) {
    val iconColor =
        if (isChecked) MaterialTheme.colorScheme.primary
        else Color.Gray

    val backgroundColor =
        if (isChecked) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.background

    Crossfade(
        targetState = Pair(iconColor, backgroundColor),
        animationSpec = tween(500), label = ""
    ) { (crossfadeIconColor, crossfadeBackgroundColor) ->
        Row(
            Modifier
                .height(56.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable { onChange(!isChecked) }
                .background(
                    color = crossfadeBackgroundColor,
                    shape = RoundedCornerShape(8.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = crossfadeIconColor,
                modifier = Modifier
                    .size(52.dp)
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            )
            Text(description)
            Spacer(Modifier.weight(1f))
            Switch(
                modifier = Modifier.padding(all = 5.dp),
                checked = isChecked,
                onCheckedChange = onChange
            )
        }
    }

}

@Composable
fun OptionList(
    chatboxViewModel: ChatboxViewModel = viewModel(factory = ChatboxViewModel.Factory),
    uiState: MessengerUiState = MessengerUiState(),
    useChipsOptions: Boolean = true,
    modifier: Modifier = Modifier
) {

    val optionIcons = Icons.Rounded

    val options: List<Option> = listOf(
        Option(
            "Real-time Message",
            "Real-time",
            optionIcons.FastForward,
            uiState.isRealtimeMsg,
            chatboxViewModel::onRealtimeMsgChanged
        ),
        Option(
            "Trigger Notification SFX",
            "Sound",
            optionIcons.NotificationsActive,
            uiState.isTriggerSFX,
            chatboxViewModel::onTriggerSfxChanged
        ),
        Option(
            "Send Message Immediately",
            "Direct",
            optionIcons.Send,
            uiState.isSendImmediately,
            chatboxViewModel::onSendImmediatelyChanged
        )
    )

    val optionModifier = modifier.padding(vertical = 4.dp, horizontal = 12.dp)

    if (useChipsOptions) {
        val horizontalScrollState = rememberScrollState()

        Row(
            optionModifier.horizontalScroll(horizontalScrollState),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                ChipOption(option.shortDescription, option.icon, option.isChecked, option.onChange)
            }
        }
    } else {
        Column(
            optionModifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                BarOption(option.description, option.icon, option.isChecked, option.onChange)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChipOptionPreview() {
    val isChecked = remember { mutableStateOf(true) }
    ChipOption(
        "Description",
        Icons.Rounded.Check,
        isChecked.value
    ) { isChecked.value = !isChecked.value }
}

@Preview(showBackground = true)
@Composable
fun ChipsOptionListPreview() {
    OptionList(useChipsOptions = true)
}

@Preview(showBackground = true)
@Composable
fun BarOptionPreview() {
    val isChecked = remember { mutableStateOf(true) }
    BarOption(
        "Description",
        Icons.Rounded.Check,
        isChecked.value
    ) { isChecked.value = !isChecked.value }
}

@Preview(showBackground = true)
@Composable
fun BarOptionListPreview() {
    OptionList(useChipsOptions = false)
}