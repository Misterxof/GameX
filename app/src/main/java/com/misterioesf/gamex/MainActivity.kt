package com.misterioesf.gamex

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.misterioesf.gamex.model.Event
import com.misterioesf.gamex.model.Point

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    private var subscribeHolder: SubscribeHolder? = null

    private val moveUpdateListener = object : MoveUpdateListener {
        override fun onPositionUpdate(movementVector: Point)  {
            subscribeHolder?.let {
                it.publish(Event.POSITION, movementVector)
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
        subscribeHolder?.let {
            it.subscribe(Event.POSITION, gameView)
        }

        val joyStick = findViewById<JoyStick>(R.id.joyStick)
        joyStick.setMoveUpdateListener(moveUpdateListener)
    }

    override fun onResume() {
        super.onResume()
        gameView.resumeGame()
    }

    override fun onPause() {
        super.onPause()
        gameView.pauseGame()
    }
}