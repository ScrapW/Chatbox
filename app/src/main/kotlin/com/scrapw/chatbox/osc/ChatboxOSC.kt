package com.scrapw.chatbox.osc

import android.util.Log
import com.illposed.osc.OSCMessage
import com.illposed.osc.transport.udp.OSCPortOut
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.InetAddress
import java.net.UnknownHostException

class ChatboxOSC(
    ipAddress: String,
    var port: Int
) {

    var addressResolvable = true
        private set

    var ipAddress = ipAddress
        set(value) {
            Log.d("IP", "IP Address $field -> $value")

            field = value
            try {
                inetAddress = InetAddress.getByName(value)
                addressResolvable = true
            } catch (e: UnknownHostException) {
                Log.d("IP", "Can't resolve $value")
                addressResolvable = false
            }
        }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            this@ChatboxOSC.ipAddress = ipAddress
        }
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
            val sender = OSCPortOut(inetAddress, port)
            delay(delay)
            try {
                sender.send(message)
            } catch (e: Exception) {
                Log.d("OSC_Send", "Can't send OSC Message")
            }
            sender.close()
        }
    }

    fun sendMessage(text: String, sendImmediately: Boolean, triggerSFX: Boolean) {
        sendOscMessage("/chatbox/input", listOf(text, sendImmediately, triggerSFX))
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
