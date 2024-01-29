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
import com.scrapw.chatbox.ChatboxApplication
import com.scrapw.chatbox.data.UserPreferencesRepository
import com.scrapw.chatbox.osc.ChatboxOSC
import com.scrapw.chatbox.ui.mainScreen.ConversationUiState
import com.scrapw.chatbox.ui.mainScreen.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant

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

    val conversationUiState = ConversationUiState()

    val messengerUiState: StateFlow<MessengerUiState> = combine(
        userPreferencesRepository.ipAddress,
        userPreferencesRepository.isRealtimeMsg,
        userPreferencesRepository.isTriggerSfx,
        userPreferencesRepository.isSendImmediately
    ) { ipAddress, isRealtimeMsg, isTriggerSFX, isSendImmediately ->
        MessengerUiState(
            ipAddress = ipAddress,
            isRealtimeMsg = isRealtimeMsg,
            isTriggerSFX = isTriggerSFX,
            isSendImmediately = isSendImmediately
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MessengerUiState()
    )


    private val chatboxOSC = ChatboxOSC(
        ipAddress = messengerUiState.value.ipAddress,
        port = 9000
    )

    val ipAddressText = mutableStateOf(messengerUiState.value.ipAddress)
    val messageText = mutableStateOf(TextFieldValue(""))

    fun onIpAddressChange(ip: String) {
        ipAddressText.value = ip
    }

    fun ipAddressApply() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                chatboxOSC.ipAddress = ipAddressText.value
                isAddressResolvable.value = chatboxOSC.addressResolvable
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.saveIpAddress(ipAddressText.value)
        }
    }


    val isAddressResolvable = mutableStateOf(true)
    fun onMessageTextChange(message: TextFieldValue) {
        messageText.value = message
        if (messengerUiState.value.isRealtimeMsg) {
            chatboxOSC.sendRealtimeMessage(message.text)
        } else {
            chatboxOSC.typing = message.text.isNotEmpty()
        }
    }

    fun onRealtimeMsgChanged(isChecked: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveIsRealtimeMsg(isChecked)
        }
    }

    fun onTriggerSfxChanged(isChecked: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveIsTriggerSFX(isChecked)
        }
    }

    fun onSendImmediatelyChanged(isChecked: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveIsSendImmediately(isChecked)
        }
    }

    fun sendMessage() {
        chatboxOSC.sendMessage(
            messageText.value.text,
            messengerUiState.value.isSendImmediately,
            messengerUiState.value.isTriggerSFX
        )
        chatboxOSC.typing = false

        conversationUiState.addMessage(
            Message(
                messageText.value.text,
                Instant.now()
            )
        )

        messageText.value = TextFieldValue("", TextRange.Zero)
    }
}

data class MessengerUiState(
    val ipAddress: String = "127.0.0.1",
    val isRealtimeMsg: Boolean = false,
    val isTriggerSFX: Boolean = true,
    val isSendImmediately: Boolean = true
)