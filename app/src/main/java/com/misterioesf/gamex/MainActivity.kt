package com.misterioesf.gamex

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.misterioesf.gamex.model.Point

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    private var subscribeHolder: SubscribeHolder? = null

    private val moveUpdateListener = object : MoveUpdateListener {
        override fun onPositionUpdate(movementVector: Point)  {
            subscribeHolder?.let {
                it.publish("vector", movementVector)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)



        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()


        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

       // gameView = GameView(this)
//        setContentView(gameView)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        subscribeHolder = SubscribeHolder()
        gameView = findViewById<GameView>(R.id.surfaceView)
        subscribeHolder?.let {
            it.subscribe("vector", gameView)
        }

        //gameView.x = 100f

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