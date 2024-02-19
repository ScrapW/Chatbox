package com.scrapw.chatbox.ui.mainScreen

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.toMutableStateList
import java.time.Instant

class ConversationUiState(
    initialMessages: List<Message> = listOf()
) {
    private val _messages: MutableList<Message> = initialMessages.toMutableStateList()
    val messages: List<Message> = _messages

    fun addMessage(msg: Message) {
        if (msg.content.isEmpty()) return
        _messages.add(0, msg.copy(no = messages.size))
    }
}

@Immutable
data class Message(
    val content: String,
    val stashed: Boolean = false,
    val timestamp: Instant? = null,
    val no: Int = -1
)