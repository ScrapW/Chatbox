package com.scrapw.chatbox.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.scrapw.chatbox.Chatbox
import com.scrapw.chatbox.ChatboxApplication
import com.scrapw.chatbox.data.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

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
        userPreferencesRepository.isRealtimeMsg,
        userPreferencesRepository.isTriggerSfx,
        userPreferencesRepository.isSendImmediately
    ) { ipAddress, isRealtimeMsg, isTriggerSFX, isSendImmediately ->
        ChatboxUiState(
            ipAddress = ipAddress,
            isRealtimeMsg = isRealtimeMsg,
            isTriggerSFX = isTriggerSFX,
            isSendImmediately = isSendImmediately
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
            },
            isTriggerSFX = runBlocking {
                userPreferencesRepository.isTriggerSfx.first()
            },
            isSendImmediately = runBlocking {
                userPreferencesRepository.isSendImmediately.first()
            }
        )
    )

    val chatbox = Chatbox(
        ipAddress = uiState.value.ipAddress,
        realtimeMsg = uiState.value.isRealtimeMsg,
        triggerSFX = uiState.value.isTriggerSFX,
        sendImmediately = uiState.value.isSendImmediately
    )

    val ipAddressText = mutableStateOf(uiState.value.ipAddress)
    val messageText = mutableStateOf(TextFieldValue(""))

//    val isRealtimeMsgEnabled = mutableStateOf(chatbox.realtimeMsg)

    fun onIpAddressChange(ip: String) {
        ipAddressText.value = ip
    }

    fun ipAddressApply() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                chatbox.ipAddress = ipAddressText.value
                isAddressResolvable.value = chatbox.addressResolvable
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.saveIpAddress(ipAddressText.value)
        }
    }


    val isAddressResolvable = mutableStateOf(true)
    fun onMessageTextChange(message: TextFieldValue) {
        messageText.value = message
        if (chatbox.realtimeMsg) {
            chatbox.sendRealtimeMessage(message.text)
        } else {
            chatbox.typing = message.text.isNotEmpty()
        }
    }

    fun onRealtimeMsgChanged(isChecked: Boolean) {
        chatbox.realtimeMsg = isChecked
        viewModelScope.launch {
            userPreferencesRepository.saveIsRealtimeMsg(isChecked)
        }
    }

    fun onTriggerSfxChanged(isChecked: Boolean) {
        chatbox.triggerSFX = isChecked
        viewModelScope.launch {
            userPreferencesRepository.saveIsTriggerSFX(isChecked)
        }
    }

    fun onSendImmediatelyChanged(isChecked: Boolean) {
        chatbox.sendImmediately = isChecked
        viewModelScope.launch {
            userPreferencesRepository.saveIsSendImmediately(isChecked)
        }
    }

    fun sendMessage() {
        chatbox.sendMessage(messageText.value.text)
        chatbox.typing = false
        messageText.value = TextFieldValue("", TextRange.Zero)
    }
}

data class ChatboxUiState(
    val ipAddress: String = "127.0.0.1",
    val isRealtimeMsg: Boolean = false,
    val isTriggerSFX: Boolean = true,
    val isSendImmediately: Boolean = true
)
