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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.illposed.osc.OSCMessage
import com.illposed.osc.transport.udp.OSCPortOut
import com.scrapw.chatbox.ui.theme.ChatboxTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    }

}

val chatbox = Chatbox()

@Composable
fun IpInputBox(modifier: Modifier = Modifier) {
    val (text, setText) = remember { mutableStateOf("127.0.0.1") }

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
    val (text, setText) = remember { mutableStateOf("") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = {
                setText(it)
                chatbox.typing = it.isNotEmpty()
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