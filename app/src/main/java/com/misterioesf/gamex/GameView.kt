package com.misterioesf.gamex

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.LinearLayout
import com.misterioesf.gamex.model.Point

class GameView : SurfaceView, SurfaceHolder.Callback, Subscriber {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var thread: GameThread? = null
    private var ballX: Float = 100f
    private var ballY: Float = 100f
    private var ballRadius: Float = 30f
    private var speedX: Float = 5f
    private var speedY: Float = 5f
    private lateinit var player: Player
    private lateinit var joyStick: JoyStick
    private var viewsList = mutableListOf<View>()

    init {
        holder.addCallback(this)

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
//        joyStick = JoyStick(this.context)
//        joyStick.measure(150, 150)
//        joyStick.setOnTouchListener { view, motionEvent ->
//            when {
//                motionEvent?.action == MotionEvent.ACTION_DOWN -> {
//                    Log.e("W", "touch x = ${motionEvent?.x} y = ${motionEvent?.y}")
//                }
//            }
//            true
//        }
//        viewsList.add(joyStick)
        player = Player(this.context)

        viewsList.add(player)

        thread = GameThread(holder, this)
        thread?.running = true
        thread?.start()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when {
            event?.action == MotionEvent.ACTION_DOWN -> {
               // Log.e("E", "touch x = ${event?.x} y = ${event?.y}")
            }
        }
        return super.onTouchEvent(event)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                thread?.join()
                retry = false
            } catch (e: InterruptedException) {
                // Retry
            }
        }
    }

    fun resumeGame() {
        thread?.running = true
    }

    fun pauseGame() {
        thread?.running = false
    }

    override fun update(event: String?, data: Any?) {
       // Log.e("GAME", "UPDATE")
        when (event) {
            "vector" -> {
                val point = data as Point
                player.update(point)
            }
        }
    }

    fun updateBall() {
        ballX += speedX
        ballY += speedY

        // Bounce the ball when it hits the screen edges
        if (ballX - ballRadius < 0 || ballX + ballRadius > width) {
            speedX = -speedX
        }

        if (ballY - ballRadius < 0 || ballY + ballRadius > height) {
            speedY = -speedY
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawColor(Color.BLACK)
        val paint = Paint()
        paint.color = Color.WHITE
        canvas.drawCircle(ballX, ballY, ballRadius, paint)
        viewsList.forEach { view -> view.draw(canvas) }
    }


}