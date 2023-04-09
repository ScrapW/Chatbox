package com.scrapw.chatbox

import android.util.Log
import com.illposed.osc.OSCMessage
import com.illposed.osc.transport.udp.OSCPortOut
import kotlinx.coroutines.*
import java.net.InetAddress
import java.net.UnknownHostException

class Chatbox(ipAddress: String, realtimeMsg: Boolean) {
    var ipAddress = ipAddress
        set(value) {
            field = value
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    try {
                        inetAddress = InetAddress.getByName(value)
                    } catch (e: UnknownHostException) {
                        Log.d("IP", "Can't resolve $value")
                    }
                }
            }
        }

    var oscPort = 9000
    var realtimeMsg = realtimeMsg

    init {
        this.ipAddress = ipAddress
    }

    private lateinit var inetAddress: InetAddress

    var typing = false
        set(value) {
            field = value
            sendOscMessage("/chatbox/typing", listOf(value))
        }

    private fun sendOscMessage(address: String, arguments: List<Any?>, delay: Long = 0) {
        CoroutineScope(Dispatchers.IO).launch {

            val message = OSCMessage(address, arguments)
            val sender = OSCPortOut(inetAddress, oscPort)
            delay(delay)
            sender.send(message)
            sender.close()
        }
    }

    fun sendMessage(text: String) {
        sendOscMessage("/chatbox/input", listOf(text, true, true))
        latestMsgTimestamp = System.currentTimeMillis()
    }

    private var realtimeMsgJob: Job? = null
    private var latestMsgTimestamp: Long = 0
    private var realtimeMsgInterval = 1500


    fun sendRealtimeMessage(text: String) {
        realtimeMsgJob?.cancel()

        Log.d(
            "Chatbox",
            "$latestMsgTimestamp  ${System.currentTimeMillis()}  ${(System.currentTimeMillis() - latestMsgTimestamp)}"
        )

        realtimeMsgJob = CoroutineScope(Dispatchers.IO).launch {
            val timeStamp = System.currentTimeMillis()

            if (timeStamp - latestMsgTimestamp < realtimeMsgInterval) {
                delay(realtimeMsgInterval - (timeStamp - latestMsgTimestamp))
            }

            sendOscMessage("/chatbox/input", listOf(text, true, false))
            sendOscMessage("/chatbox/typing", listOf(text.isNotEmpty()), 50)

            latestMsgTimestamp = System.currentTimeMillis()
        }
    }
}
