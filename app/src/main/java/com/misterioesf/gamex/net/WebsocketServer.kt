package com.misterioesf.gamex.net

import android.util.Log
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.lang.Exception
import java.net.InetSocketAddress

const val WEBSOCKET_SERVER = "WebsocketServer"
class WebsocketServer(address: InetSocketAddress): WebSocketServer(address) {
    val connections = HashSet<WebSocket>()
    var host: WebSocket? = null
    var client: WebSocket? = null
    var isHosted = false

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
       // Log.e(WEBSOCKET_SERVER, "onOpen")
       // Log.e(WEBSOCKET_SERVER, "onOpen ${conn?.getRemoteSocketAddress()}")
      //  Log.e(WEBSOCKET_SERVER, "onOpen ${conn?.toString()}")
        conn?.let { connections.add(it) }

        if (!isHosted) {
            host = conn
            isHosted = !isHosted
        } else client = conn
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        Log.e(WEBSOCKET_SERVER, "onClose")
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
       // Log.e(WEBSOCKET_SERVER, "onMessage")
       // Log.e(WEBSOCKET_SERVER, message ?: "null")
        if (conn == host) {
            client?.send(message)
        } else {
            host?.send(message)
        }
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        Log.e(WEBSOCKET_SERVER, "onError ")
        Log.e(WEBSOCKET_SERVER, ex?.printStackTrace().toString())
    }

    override fun onStart() {
        Log.e(WEBSOCKET_SERVER, "onStart")
    }
}