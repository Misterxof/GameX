package com.misterioesf.gamex.net

import android.util.Log
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

const val WEBSOCKET_CLIENT = "WebsocketClient"
class WebSocketClient(uri: URI): WebSocketClient(uri) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        Log.e(WEBSOCKET_CLIENT, "onOpen")
    }

    override fun onMessage(message: String?) {
        Log.e(WEBSOCKET_CLIENT, "onMessage")
        Log.e(WEBSOCKET_CLIENT, message ?: "null")
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        Log.e(WEBSOCKET_CLIENT, "onClose")
    }

    override fun onError(ex: Exception?) {
        Log.e(WEBSOCKET_CLIENT, "onError")
        Log.e(WEBSOCKET_CLIENT, ex?.printStackTrace().toString())
    }
}