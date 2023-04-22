package com.scrapw.chatbox.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scrapw.chatbox.ui.theme.ChatboxTheme

@Composable
fun ConversationList(
    uiState: ConversationUiState,
    onCopyPressed: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(uiState.messages) { message ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp)
                    .shadow(4.dp, RoundedCornerShape(8.dp))
            ) {
                Column(
                    Modifier.padding(16.dp)
                ) {
                    Text(
                        text = message.content,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {
                                onCopyPressed(message.content)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy message content"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConversationListPreview() {
    val messageList = listOf<Message>(
        Message(content = "Hello"),
        Message(content = "你好~"),
        Message(content = "対応する絵文字をクリックして、使用言語を選んでください。その後、 ⁠お知らせ と ⁠ルール で情報を確認してください。"),
        Message(content = "🥥🍹🍡"),
        Message(content = ""),
        Message(content = " ")
    )
    ChatboxTheme {
        ConversationList(
            ConversationUiState(
                initialMessages = messageList
            ),
            {}
        )
    }
}