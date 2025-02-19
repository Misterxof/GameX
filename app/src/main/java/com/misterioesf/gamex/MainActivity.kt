package com.misterioesf.gamex

import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.misterioesf.gamex.model.Event
import com.misterioesf.gamex.model.Point
import com.misterioesf.gamex.net.WebSocketClient
import com.misterioesf.gamex.net.WebsocketServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.InetSocketAddress
import java.net.URI


class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    private var subscribeHolder: SubscribeHolder? = null
    private lateinit var server: WebsocketServer
    private lateinit var client: WebSocketClient

    private val moveUpdateListener = object : MoveUpdateListener {
        override fun onPositionUpdate(movementVector: Point)  {
            subscribeHolder?.let {
                it.publish(Event.POSITION, movementVector)
            }
        }
    }

    private val gameEventListener = object : GameEventListener {
        override fun onGameOver() {
            runOnUiThread {
                Log.e("MAIn" , "DIALOG")
                AlertDialog.Builder(this@MainActivity)
                    .setMessage("Game over")
                    .setPositiveButton("Restart") { d,id ->
                        gameView.resumeGame()
                        d.cancel()
                    }.setNegativeButton("No") { d, id ->
                        d.cancel()
                    }.show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        subscribeHolder = SubscribeHolder()
        gameView = findViewById<GameView>(R.id.surfaceView)
        gameView.setGameEventListener(gameEventListener)
        subscribeHolder?.let {
            it.subscribe(Event.POSITION, gameView)
            it.subscribe(Event.COLLISION, gameView)
        }

        val joyStick = findViewById<JoyStick>(R.id.joyStick)
        joyStick.setMoveUpdateListener(moveUpdateListener)

        val spawnButton = findViewById<FloatingActionButton>(R.id.spawnButton)
        spawnButton.setOnClickListener {
            subscribeHolder?.let {
                Log.e("Tt", "PUB")
                it.publish(Event.COLLISION, 0)
            }
        }

        initServer()
    }

    fun initServer(){
        val manager = super.getSystemService(WIFI_SERVICE) as WifiManager
        val dhcp = manager.dhcpInfo
        val address: String = Formatter.formatIpAddress(dhcp.ipAddress)

        Log.e("MAIN", address)
        CoroutineScope(Job() + Dispatchers.IO).launch {
            server = WebsocketServer(InetSocketAddress("$address", 8887))
            server.run()
        }

        Log.e("MAIN", "MIDDLE")

        CoroutineScope(Job() + Dispatchers.IO).launch {
            delay(5000)
            client = WebSocketClient(URI("ws://$address:8887"))
            client.connect()
        }
    }

    override fun onResume() {
        super.onResume()
        gameView.resumeGame()
    }

    override fun onPause() {
        super.onPause()
        gameView.pauseGame()
    }

    override fun onStop() {
        super.onStop()
        TODO()
    }
}