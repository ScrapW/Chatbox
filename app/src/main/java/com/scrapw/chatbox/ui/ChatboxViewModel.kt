package com.scrapw.chatbox.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.scrapw.chatbox.Chatbox
import com.scrapw.chatbox.ChatboxApplication
import com.scrapw.chatbox.data.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ChatboxViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ChatboxApplication)
                ChatboxViewModel(application.userPreferencesRepository)
            }
        }
    }


    val uiState: StateFlow<ChatboxUiState> = combine(
        userPreferencesRepository.ipAddress,
        userPreferencesRepository.isRealtimeMsg
    ) { ipAddress, isRealtimeMsg ->
        ChatboxUiState(
            ipAddress = ipAddress,
            isRealtimeMsg = isRealtimeMsg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ChatboxUiState(
            ipAddress = runBlocking {
                userPreferencesRepository.ipAddress.first()
            },
            isRealtimeMsg = runBlocking {
                userPreferencesRepository.isRealtimeMsg.first()
            }
        )
    )

    val chatbox = Chatbox(
        ipAddress = uiState.value.ipAddress,
        realtimeMsg = uiState.value.isRealtimeMsg
    )

    val ipAddress = mutableStateOf(uiState.value.ipAddress)
    val messageText = mutableStateOf("")

//    val isRealtimeMsgEnabled = mutableStateOf(chatbox.realtimeMsg)

    fun onIpAddressChange(ip: String) {
        ipAddress.value = ip
    }

    fun ipAddressApply() {
        chatbox.ipAddress = ipAddress.value
        viewModelScope.launch {
            userPreferencesRepository.saveIpAddress(ipAddress.value)
        }
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
        chatbox.realtimeMsg = isChecked
//        isRealtimeMsgEnabled.value = isChecked
        viewModelScope.launch {
            userPreferencesRepository.saveIsRealtimeMsg(isChecked)
        }
    }

    fun sendMessage() {
        chatbox.sendMessage(messageText.value)
        chatbox.typing = false
        messageText.value = ""
    }
}

data class ChatboxUiState(
    val ipAddress: String = "127.0.0.1",
    val isRealtimeMsg: Boolean = false
)
