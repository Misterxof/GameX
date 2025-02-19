package com.misterioesf.gamex

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AlertDialog
import com.misterioesf.gamex.model.Apple
import com.misterioesf.gamex.model.Collision
import com.misterioesf.gamex.model.Event
import com.misterioesf.gamex.model.GameObject
import com.misterioesf.gamex.model.Point
import com.misterioesf.gamex.model.Rect
import com.misterioesf.gamex.model.ScreenPart
import com.misterioesf.gamex.model.TestObject
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
    private var gameEventListener: GameEventListener? = null
    var apple: Apple? = null
    val paint = Paint()
    private val gameWidth = 2000f
    private val gameHeight = 1000f
    private var vecX = 0f
    private var vecY = 0f
    private lateinit var rect: Rect
    private lateinit var topWall: Rect
    private lateinit var bottomWall: Rect
    private lateinit var leftWall: Rect
    private lateinit var rightWall: Rect
    val t: TestObject
    private var travel = Point(0f, 0f)

    init {
        holder.addCallback(this)
        t = TestObject(Point(100f, 100f), context, 1)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
//        player = Player(Point(Random.nextInt(5,(gameWidth - 5).toInt()).toFloat(),
//            Random.nextInt(5, (gameHeight - 5).toInt()).toFloat()),
//            Point(width / 2f, height / 2f), this.context)
        player = Player(Point(150f,150f),
            Point(width / 2f, height / 2f), this.context)
        initWalls()
        eventSubscriber.subscribe(Event.COLLISION, player)

        gameObjects.add(player)
        gameObjects.add(t)

        thread = GameThread(holder, this)
        thread?.resumeThread()
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
        Log.e("GAME", "SPAWN ${thread == null}")
        thread?.resumeThread()
        thread?.run()
    }

    fun pauseGame() {
        thread?.stopThread()
    }

    fun stopThread() {
        thread?.interrupt()
        thread?.join()
    }

    fun initWalls() {
        rect = Rect(700f - vecX, 700f - vecY, 700f + 200f - vecX, 700f + 200f - vecY, player.offsetPoint)
        topWall = Rect(0f, 0f, gameWidth, 5f, player.offsetPoint)
        bottomWall = Rect(0f, gameHeight, gameWidth, gameHeight - 5f, player.offsetPoint)
        leftWall = Rect(0f, 0f, 5f, gameHeight, player.offsetPoint)
        rightWall = Rect(gameWidth, 0f, gameWidth - 5f, gameHeight, player.offsetPoint)
    }

    fun collisionChecker() {
        if (apple != null) {
            val playerPos = player.position
            val applePos = apple!!.position

            val dist = getDistance(playerPos, applePos)

            if (dist <= player.radius + apple!!.radius) {
                eventSubscriber.publish(Event.COLLISION, Collision.HEALS)
                gameObjects.remove(apple as GameObject)
                apple = null
                appleSpawn()
            }

            if (player.positionHistory.x - player.radius < 0 || player.positionHistory.x + player.radius > gameWidth) {
                gameEventListener?.onGameOver()
                pauseGame()
            }

            if (player.positionHistory.y - player.radius < 0 || player.positionHistory.y + player.radius > gameHeight) {
                gameEventListener?.onGameOver()
                pauseGame()
            }
        }
    }

    fun appleSpawn() {
        if (apple == null) {
            val playerPart = getScreenPart(player.positionHistory, gameWidth.toInt(), gameHeight.toInt())
            val newPosition = randomPart(playerPart, gameWidth.toInt(), gameHeight.toInt())
            newPosition.x += player.moveHistory.x
            newPosition.y += player.moveHistory.y
            newPosition.x += player.offsetPoint.x
            newPosition.y += player.offsetPoint.y

            apple = Apple(newPosition, context)
            gameObjects.add(apple!!)
            Log.e("GAME", "SPAWN ${apple.toString()}")
            Log.e("GAME", "SPAWN w = $gameWidth h = $gameHeight")
            Log.e("GAME", "SPAWN x = ${player.positionHistory.x} y = ${player.positionHistory.y}")
        }
    }

    fun setGameEventListener(gameEventListener: GameEventListener) {
        this.gameEventListener = gameEventListener
    }

    override fun update(event: Event, data: Any?) {
        when (event) {
            Event.POSITION -> {
                val point = data as Point
//                player.updatePosition(point)  // if update here values are incorrect
                vecX = point.x * player.speed
                vecY = point.y * player.speed


            }

            Event.COLLISION -> {
               // val test = TestObject(Point(100f, 100f), context)
                val test2 = TestObject(Point(100f + player.moveHistory.x, 100f + player.moveHistory.y), context, 0)
                val test = TestObject(Point(100f + topWall.left, 100f + topWall.top), context, 0)
                Log.e("GAME", "1x = ${player.moveHistory.x} y = ${player.moveHistory.y}")
                Log.e("GAME", "2x = ${player.positionHistory.x} y = ${player.positionHistory.y}")
                Log.e("GAME", "3x = ${travel.x} y = ${travel.y}")
                Log.e("GAME", "4x = ${player.positionHistory.x} y = ${player.positionHistory.y}")
               // gameObjects.add(test)
                gameObjects.add(test2)
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

    fun updateWalls() {
        travel.x -= vecX
        travel.y -= vecY
        player.updatePosition(Point(vecX / player.speed, vecY / player.speed))

        topWall.update(vecX, vecY)
        bottomWall.update(vecX, vecY)
        leftWall.update(vecX, vecY)
        rightWall.update(vecX, vecY)
    }

    fun updateAll() {
        gameObjects.forEach {
            it.updatePosition(vecX, vecY)
        }
    }

    fun drawRectObject(canvas: Canvas, rect: Rect){
        canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, paint)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawColor(Color.BLACK)

        updateAll()

        paint.color = Color.WHITE
        canvas.drawCircle(ballX, ballY, ballRadius, paint)
        gameObjects.forEach { view -> view.draw(canvas) }

        paint.color = Color.YELLOW
        rect.update(vecX, vecY)

        canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, paint)

//        apple?.let { it.updateScreen(vecX, vecY) }
        if (apple != null) apple!!.draw(canvas)



        //  Game arena
        paint.color = Color.LTGRAY
        updateWalls()
        drawRectObject(canvas, topWall)
        drawRectObject(canvas, bottomWall)
        drawRectObject(canvas, leftWall)
        drawRectObject(canvas, rightWall)

        //  UI
        paint.color = Color.WHITE
        paint.textSize = 40f
        canvas?.drawText("Score: ${player.getSize()}", width - 240f, 50f, paint)
    }
}