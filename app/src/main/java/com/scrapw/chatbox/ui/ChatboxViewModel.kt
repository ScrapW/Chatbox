package com.scrapw.chatbox.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.scrapw.chatbox.Chatbox

class ChatboxViewModel : ViewModel() {
    val chatbox = Chatbox()

    val ipAddress = mutableStateOf(chatbox.ipAddress)
    val messageText = mutableStateOf("")

    val isRealtimeMsgEnabled = mutableStateOf(chatbox.realtimeMsg)

    fun onIpAddressChange(ip: String) {
        chatbox.ipAddress = ip
        ipAddress.value = ip
    }

    fun onMessageTextChange(text: String) {
        messageText.value = text
        if (chatbox.realtimeMsg) {
            chatbox.sendRealtimeMessage(text)
        } else {
            chatbox.typing = text.isNotEmpty()
        }
    }

    fun onRealtimeMsgChanged(isChecked: Boolean) {
        isRealtimeMsgEnabled.value = isChecked
        chatbox.realtimeMsg = isChecked
    }

    fun sendMessage() {
        chatbox.sendMessage(messageText.value)
        chatbox.typing = false
        messageText.value = ""
    }
}
