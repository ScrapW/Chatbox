package com.scrapw.chatbox.ui.mainScreen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Redo
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scrapw.chatbox.R
import com.scrapw.chatbox.data.SettingsStates
import com.scrapw.chatbox.ui.common.HapticConstants
import com.scrapw.chatbox.ui.theme.ChatboxTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
private fun MessageTime(time: Instant?, modifier: Modifier = Modifier) {
    if (time == null) return
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    Text(
        text = time.atZone(ZoneId.systemDefault()).format(formatter),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.outline,
        modifier = modifier
    )
}

@Composable
private fun StashedTag(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.stashed),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.outline,
        modifier = modifier
    )
}

@Composable
private fun CopyButton(message: Message, onCopyPressed: (TextFieldValue) -> Unit) {
    IconButton(
        modifier = Modifier.size(30.dp),
        onClick = {
            onCopyPressed(
                TextFieldValue(
                    message.content,
                    TextRange(message.content.length)
                )
            )
        }
    ) {
        if (!message.stashed) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = stringResource(R.string.message_card_copy),
                modifier = Modifier.size(20.dp)
            )
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Redo,
                contentDescription = stringResource(R.string.message_card_restore),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun MessageCard(
    message: Message,
    onCopyPressed: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clip(CardDefaults.shape)
            .clickable {
                onCopyPressed(
                    TextFieldValue(
                        message.content,
                        TextRange(message.content.length)
                    )
                )
            }
    ) {
        Column(
            Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            if (!message.stashed) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontStyle = FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                MessageTime(message.timestamp, Modifier.align(Alignment.Bottom))
                Spacer(modifier = Modifier.weight(1f))
                if (message.stashed) {
                    StashedTag(Modifier.align(Alignment.Bottom))
                    Spacer(Modifier.width(12.dp))
                }
                CopyButton(message, onCopyPressed)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ConversationList(
    uiState: ConversationUiState,
    onCopyPressed: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        modifier = modifier,
        reverseLayout = true,
        state = lazyListState
    ) {
        // If I don't add this, it won't keep scrolling.
        item(key = -1) {
            Spacer(Modifier.height(12.dp))
        }

        items(uiState.messages, key = { it.no }) { message ->
            MessageCard(message, onCopyPressed, Modifier.animateItemPlacement())
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmptyConversationList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.History,
            contentDescription = null,
            modifier = Modifier.size(140.dp),
            tint = MaterialTheme.colorScheme.primaryContainer
        )
        Text(
            text = stringResource(R.string.empty_conversation),
            style = MaterialTheme.typography.titleLarge,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.primaryContainer
        )
    }
}

@Composable
fun Conversation(
    uiState: ConversationUiState,
    onCopyPressed: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val buttonHapticState = SettingsStates.buttonHapticState()


    Box(modifier) {
        Crossfade(
            targetState = uiState.messages.isEmpty(),
            animationSpec = tween(500), label = "MainScreenConversationCrossfade"
        ) { conversationEmpty ->
            if (conversationEmpty) {
                EmptyConversationList(Modifier.fillMaxSize())
            } else {
                ConversationList(
                    uiState = uiState,
                    onCopyPressed = {
                        onCopyPressed(it)
                        if (buttonHapticState.value) {
                            view.performHapticFeedback(HapticConstants.button)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConversationListPreview() {
    val messageList = listOf<Message>(
        Message(content = "Hello ███", timestamp = Instant.now()),
        Message(content = "你好~"),
        Message(content = "対応する絵文字をクリックして、使用言語を選んでください。その後、 ⁠お知らせ と ⁠ルール で情報を確認してください。"),
        Message(content = "🥥🍹🍡"),
        Message(content = "Line1\nLine2")
    )
    ChatboxTheme {
        ConversationList(
            ConversationUiState(
                initialMessages = messageList.reversed()
            ),
            {}
        )
    }
}