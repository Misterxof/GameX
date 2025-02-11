package com.misterioesf.gamex

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.misterioesf.gamex.model.Apple
import com.misterioesf.gamex.model.Collision
import com.misterioesf.gamex.model.Event
import com.misterioesf.gamex.model.GameObject
import com.misterioesf.gamex.model.Point
import com.misterioesf.gamex.model.ScreenPart
import kotlin.random.Random

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
    private var gameObjects = mutableListOf<GameObject>()
    private val eventSubscriber = SubscribeHolder()
    var apple: Apple? = null

    init {
        holder.addCallback(this)

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        player = Player(this.context)
        eventSubscriber.subscribe(Event.COLLISION, player)
        gameObjects.add(player)

        thread = GameThread(holder, this)
        thread?.running = true
        thread?.start()
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

    fun collisionChecker() {
        if (apple != null) {
            val playerPos = player.position
            val applePos = apple!!.position

            val dist = getDistance(playerPos, applePos)

            if (dist <= player.radius + apple!!.radius) {
                eventSubscriber.publish(Event.COLLISION, Collision.HEALS)
                apple = null
                appleSpawn()
            }
        }
    }

    fun appleSpawn() {
        if (apple == null) {
            val playerPart = getScreenPart(player.position, width, height)
            val newPosition = randomPart(playerPart, width, height)
            apple = Apple(newPosition, context)
            Log.e("GAME", "SPAWN ${apple.toString()}")
        }
    }

    override fun update(event: Event, data: Any?) {
        when (event) {
            Event.POSITION -> {
                val point = data as Point
                player.updatePosition(point)
            }

            Event.COLLISION -> {}
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
        gameObjects.forEach { view -> view.draw(canvas) }

        if (apple != null) apple!!.draw(canvas)
    }
}