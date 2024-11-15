package com.misterioesf.gamex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    private var subscribeHolder: SubscribeHolder? = null

    private val moveUpdateListener = object : MoveUpdateListener {
        override fun onPositionUpdate(movementVector: Pair<Float, Float>)  {
            subscribeHolder?.let {
                it.publish("vector", movementVector)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        subscribeHolder = SubscribeHolder()

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

       // gameView = GameView(this)
//        setContentView(gameView)
        gameView = findViewById<GameView>(R.id.surfaceView)
        subscribeHolder?.let {
            it.subscribe("vector", gameView)
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