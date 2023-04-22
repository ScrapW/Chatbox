package com.scrapw.chatbox.ui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.toMutableStateList

class ConversationUiState(
    initialMessages: List<Message> = listOf<Message>()
) {
    private val _messages: MutableList<Message> = initialMessages.toMutableStateList()
    val messages: List<Message> = _messages

    fun addMessage(msg: Message) {
        if (msg.content.isNotEmpty()) _messages.add(0, msg)
    }
}

@Immutable
data class Message(
    val content: String
)
