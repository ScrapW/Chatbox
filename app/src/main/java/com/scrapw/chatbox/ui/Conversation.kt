package com.scrapw.chatbox.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scrapw.chatbox.ui.theme.ChatboxTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MessageTime(time: Instant?, modifier: Modifier = Modifier) {
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
fun CopyButton(message: Message, onCopyPressed: (TextFieldValue) -> Unit) {
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
        Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Copy message content",
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun MessageCard(message: Message, onCopyPressed: (TextFieldValue) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
    ) {
        Column(
            Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                MessageTime(message.timestamp, Modifier.align(Alignment.Bottom))
                Spacer(modifier = Modifier.weight(1f))
                CopyButton(message, onCopyPressed)
            }
        }
    }
}

@Composable
fun ConversationList(
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
        items(uiState.messages) { message ->
            MessageCard(message, onCopyPressed)
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