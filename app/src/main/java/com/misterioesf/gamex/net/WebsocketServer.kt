package com.misterioesf.gamex.net

import android.util.Log
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.lang.Exception
import java.net.InetSocketAddress

const val WEBSOCKET_SERVER = "WebsocketServer"
class WebsocketServer(address: InetSocketAddress): WebSocketServer(address) {
    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        Log.e(WEBSOCKET_SERVER, "onOpen")
        Log.e(WEBSOCKET_SERVER, "onOpen ${conn?.getRemoteSocketAddress()}")
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        Log.e(WEBSOCKET_SERVER, "onClose")
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        Log.e(WEBSOCKET_SERVER, "onMessage")
        Log.e(WEBSOCKET_SERVER, message ?: "null")
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        Log.e(WEBSOCKET_SERVER, "onError ")
        Log.e(WEBSOCKET_SERVER, ex?.printStackTrace().toString())
    }

    override fun onStart() {
        Log.e(WEBSOCKET_SERVER, "onStart")
    }
}