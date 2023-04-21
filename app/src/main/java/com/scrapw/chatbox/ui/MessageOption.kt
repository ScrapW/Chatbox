package com.scrapw.chatbox.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Send
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

@Composable
fun Option(
    description: String,
    icon: ImageVector,
    isChecked: Boolean,
    onChange: (Boolean) -> Unit,
) {
    val iconColor = if (isChecked) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Gray
    }
    val backgroundColor = if (isChecked) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.background
    }
    Crossfade(
        targetState = Pair(iconColor, backgroundColor),
        animationSpec = tween(500)
    ) { (crossfadeIconColor, crossfadeBackgroundColor) ->
        Row(
            Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 12.dp)
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

@Preview(showBackground = true)
@Composable
fun OptionPreview() {
    val isChecked = remember { mutableStateOf(true) }
    Option(
        "Description",
        Icons.Rounded.Check,
        isChecked.value
    ) { isChecked.value = !isChecked.value }
}

@Composable
fun OptionList(
    chatboxViewModel: ChatboxViewModel,
    uiState: ChatboxUiState,
    modifier: Modifier = Modifier
) {
    val optionIcons = Icons.Rounded

    Column(modifier) {
        Option(
            "Real-time Message",
            optionIcons.FastForward,
            uiState.isRealtimeMsg,
            chatboxViewModel::onRealtimeMsgChanged
        )
        Option(
            "Trigger Notification SFX",
            optionIcons.NotificationsActive,
            uiState.isTriggerSFX,
            chatboxViewModel::onTriggerSfxChanged
        )
        Option(
            "Send Message Immediately",
            optionIcons.Send,
            uiState.isSendImmediately,
            chatboxViewModel::onSendImmediatelyChanged
        )
    }
}