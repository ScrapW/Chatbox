package com.scrapw.chatbox

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.illposed.osc.OSCMessage
import com.illposed.osc.transport.udp.OSCPortOut
import com.scrapw.chatbox.ui.theme.ChatboxTheme
import kotlinx.coroutines.*
import java.net.InetAddress

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatboxTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatScreen()
                }
            }
        }
    }
}

class Chatbox {
    var ipAddress = "127.0.0.1"
        set(value) {
            field = value
            inetAddress = InetAddress.getByName(value)
            refreshSender()
        }

    var oscPort = 9000
        set(value) {
            field = value
            refreshSender()
        }

    var realtimeUpdate = true

    private var inetAddress = InetAddress.getByName(ipAddress)
    private var sender = OSCPortOut(inetAddress, 9000)

    var typing = false
        set(value) {
            field = value
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    val message = OSCMessage("/chatbox/typing", listOf(value))
                    sender.send(message)
                }
            }
        }

    private fun refreshSender() {
        sender.close()
        sender = OSCPortOut(inetAddress, oscPort)
    }

    fun sendOscMessage(text: String = "Test!") {
        val message = OSCMessage("/chatbox/input", listOf(text, true))
        sender.send(message)
        latestMsgTimestamp = System.currentTimeMillis()
    }

    private var realtimeMsgJob: Job? = null
    private var latestMsgTimestamp: Long = 0
    private var realtimeMsgInterval = 1500


    fun sendRealtimeMessage(text: String = "Test!") {
        realtimeMsgJob?.cancel()

        Log.d(
            "Chatbox",
            latestMsgTimestamp.toString() + "  " + System.currentTimeMillis()
                .toString() + "  " + (System.currentTimeMillis() - latestMsgTimestamp).toString()
        )

        realtimeMsgJob = CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {

                val timeStamp = System.currentTimeMillis()

                if (timeStamp - latestMsgTimestamp < realtimeMsgInterval) {
                    delay(realtimeMsgInterval - (timeStamp - latestMsgTimestamp))
                }

                var message = OSCMessage("/chatbox/input", listOf(text, true))
                sender.send(message)


                message = OSCMessage("/chatbox/typing", listOf(text.isNotEmpty()))
                sender.send(message)

                latestMsgTimestamp = System.currentTimeMillis()
            }
        }

    }
}

val chatbox = Chatbox()

@Composable
fun IpInputBox(modifier: Modifier = Modifier) {
    val (text, setText) = rememberSaveable { mutableStateOf("127.0.0.1") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = setText,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Enter your ip here") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO) {
                            chatbox.ipAddress = text
                        }
                    }
                }
            )
        )
    }
}

@Composable
fun MessageInputBox(modifier: Modifier = Modifier) {
    val (text, setText) = rememberSaveable { mutableStateOf("") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = {
                setText(it)
                chatbox.typing = it.isNotEmpty()
                if (chatbox.realtimeUpdate) chatbox.sendRealtimeMessage(it)
            },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Enter your message here") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO) {
                            chatbox.sendOscMessage(text)
                            chatbox.typing = false
                            setText("")
                        }
                    }
                }
            )
        )

        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        chatbox.sendOscMessage(text)
                        setText("")
                    }
                }
            },
            modifier = Modifier.size(48.dp),
            content = {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send Message",
                    tint = Color.White
                )
            }
        )
    }
}

@Composable
fun ChatScreen() {
    Column() {
        IpInputBox()
        MessageInputBox()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    ChatboxTheme {
        ChatScreen()
    }
}