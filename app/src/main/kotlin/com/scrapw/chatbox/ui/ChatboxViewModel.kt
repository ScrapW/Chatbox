package com.scrapw.chatbox.ui

import UpdateInfo
import UpdateStatus
import android.util.Log
import androidx.annotation.MainThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import checkUpdate
import com.scrapw.chatbox.ChatboxApplication
import com.scrapw.chatbox.data.UserPreferencesRepository
import com.scrapw.chatbox.osc.ChatboxOSC
import com.scrapw.chatbox.ui.mainScreen.ConversationUiState
import com.scrapw.chatbox.ui.mainScreen.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.Instant

class ChatboxViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    companion object {
        private lateinit var instance: ChatboxViewModel

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ChatboxApplication)
                instance = ChatboxViewModel(application.userPreferencesRepository)
                Log.d("ChatboxViewModel", "Init")
                instance
            }
        }


        // https://stackoverflow.com/a/61918988
        @MainThread
        fun getInstance(): ChatboxViewModel {
            if (!isInstanceInitialized()) {
                throw Exception("ChatboxViewModel is not initialized!")
            }
            Log.d("ChatboxViewModel", "getInstance()")
            return instance
        }

        fun isInstanceInitialized(): Boolean {
            return ::instance.isInitialized
        }
    }

    override fun onCleared() {
        Log.d("ChatboxViewModel", "onCleared()")
        super.onCleared()
    }

    val conversationUiState = ConversationUiState()

    //TODO: Is this correct?
    private val storedIpState: StateFlow<String> =
        userPreferencesRepository.ipAddress.map {
            it
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    private val userInputIpState = MutableStateFlow<String>("")


    private val ipFlow = listOf(
        storedIpState,
        userInputIpState
    ).asFlow().flattenMerge()

    val messengerUiState: StateFlow<MessengerUiState> = combine(
        ipFlow,
        userPreferencesRepository.isRealtimeMsg,
        userPreferencesRepository.isTriggerSfx,
        userPreferencesRepository.isTypingIndicator,
        userPreferencesRepository.isSendImmediately
    ) { ipAddress, isRealtimeMsg, isTriggerSFX, isTypingIndicator, isSendImmediately ->
        MessengerUiState(
            ipAddress = ipAddress,
            isRealtimeMsg = isRealtimeMsg,
            isTriggerSFX = isTriggerSFX,
            isTypingIndicator = isTypingIndicator,
            isSendImmediately = isSendImmediately
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MessengerUiState()
    )


    private val remoteChatboxOSC = ChatboxOSC(
//        ipAddress = messengerUiState.value.ipAddress,
        ipAddress = runBlocking {
            userPreferencesRepository.ipAddress.first()
        },
        port = 9000
    )

    private val localChatboxOSC = ChatboxOSC(
        ipAddress = "localhost",
        port = 9000
    )

    //    var ipAddressText = messengerUiState.value.ipAddress
    val messageText = mutableStateOf(TextFieldValue(""))

    fun onIpAddressChange(ip: String) {
        userInputIpState.value = ip
    }

    fun ipAddressApply(address: String) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                remoteChatboxOSC.ipAddress = address
                isAddressResolvable.value = remoteChatboxOSC.addressResolvable
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.saveIpAddress(address)
        }
    }

    fun portApply(port: Int) {
        remoteChatboxOSC.port = port
        viewModelScope.launch {
            userPreferencesRepository.savePort(port)
        }
    }


    val isAddressResolvable = mutableStateOf(true)
    fun onMessageTextChange(message: TextFieldValue, local: Boolean = false) {
        val osc = if (!local) remoteChatboxOSC else localChatboxOSC

        messageText.value = message
        if (messengerUiState.value.isRealtimeMsg) {
            osc.sendRealtimeMessage(message.text)
        } else {
            if (messengerUiState.value.isTypingIndicator) {
                osc.typing = message.text.isNotEmpty()
            }
        }
    }

    fun sendMessage(local: Boolean = false) {
        val osc = if (!local) remoteChatboxOSC else localChatboxOSC

        osc.sendMessage(
            messageText.value.text,
            messengerUiState.value.isSendImmediately,
            messengerUiState.value.isTriggerSFX
        )
        osc.typing = false

        conversationUiState.addMessage(
            Message(
                messageText.value.text,
                false,
                Instant.now()
            )
        )

        messageText.value = TextFieldValue("", TextRange.Zero)
    }

    fun stashMessage(local: Boolean = false) {
        val osc = if (!local) remoteChatboxOSC else localChatboxOSC

        osc.typing = false

        conversationUiState.addMessage(
            Message(
                messageText.value.text,
                true,
                Instant.now()
            )
        )

        messageText.value = TextFieldValue("", TextRange.Zero)
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

    fun onTypingIndicatorChanged(isChecked: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveTypingIndicator(isChecked)
        }
    }

    fun onSendImmediatelyChanged(isChecked: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveIsSendImmediately(isChecked)
        }
    }

    private var updateChecked = false
    var updateInfo by mutableStateOf(UpdateInfo(UpdateStatus.NOT_CHECKED))
    fun checkUpdate() {
        if (updateChecked) return
        updateChecked = true

        viewModelScope.launch(Dispatchers.IO) {
            updateInfo = checkUpdate("ScrapW", "Chatbox")
        }
    }
}

data class MessengerUiState(
    val ipAddress: String = "127.0.0.1",
    val isRealtimeMsg: Boolean = false,
    val isTriggerSFX: Boolean = true,
    val isTypingIndicator: Boolean = true,
    val isSendImmediately: Boolean = true
)