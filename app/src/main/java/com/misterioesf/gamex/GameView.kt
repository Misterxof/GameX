package com.misterioesf.gamex

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.misterioesf.gamex.model.Apple
import com.misterioesf.gamex.model.Collision
import com.misterioesf.gamex.model.Event
import com.misterioesf.gamex.model.GameObject
import com.misterioesf.gamex.model.GameObjectController
import com.misterioesf.gamex.model.Point
import com.misterioesf.gamex.model.Rect
import com.misterioesf.gamex.model.TestObject
import com.misterioesf.gamex.model.Type
import com.misterioesf.gamex.model.Walls
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date
import java.time.Duration
import java.time.Instant

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
    private lateinit var gameObjectController: GameObjectController
    private val eventSubscriber = SubscribeHolder()
    private var gameEventListener: GameEventListener? = null
    var apple: Apple? = null
    val paint = Paint()
    private val mapWidth = 1500f
    private val mapHeight = 1000f
    private var vecX = 0f
    private var vecY = 0f
    private lateinit var rect: Rect
    private var walls: Walls
    private var travel = Point(0f, 0f)
    private var enemy: Enemy? = null

    init {
        holder.addCallback(this)
        walls = Walls(context)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
//        player = Player(Point(Random.nextInt(5,(gameWidth - 5).toInt()).toFloat(),
//            Random.nextInt(5, (gameHeight - 5).toInt()).toFloat()),
//            Point(width / 2f, height / 2f), this.context)
        restart()
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

    fun restart() {
        player = Player(Point(150f,150f),
            Point(width / 2f, height / 2f), this.context)
        gameObjectController = GameObjectController.getInstance(player.offsetPoint, context)
        walls.initWalls(mapWidth, mapHeight, player.offsetPoint)
        rect = Rect(700f - vecX, 700f - vecY, 700f + 200f - vecX, 700f + 200f - vecY, player.offsetPoint, context)
        eventSubscriber.subscribe(Event.COLLISION, player)

        gameObjectController.addGameObject(player)
        val bundle = Bundle()
        bundle.putInt("i", 1)
        gameObjectController.createGameObject<TestObject>(Point(100f, 100f), bundle)
        spawnEnemy()
        appleSpawn()

        thread = GameThread(holder, this)
        thread?.resumeThread()
        thread?.start()
    }

    fun resumeGame() {
        Log.e("GAME", "SPAWN ${thread == null}")
        thread?.resumeThread()
        thread?.start()
    }

    fun pauseGame() {
        thread?.stopThread()
    }

    fun stopThread() {
        thread?.interrupt()
        thread?.join()
    }

    fun collisionChecker() {
        if (apple != null) {
            val playerPos = player.positionScreen
            val applePos = apple!!.positionScreen

            val distApple = getDistance(playerPos, applePos)
            val distEnemyApple = getDistance(enemy!!.positionScreen, applePos)

            if (distApple <= player.radius + apple!!.radius) {
                eventSubscriber.publish(Event.COLLISION, Collision.HEALS, Type.PLAYER)
                gameObjectController.removeGameObject(apple as GameObject)
                apple = null
                appleSpawn()
            }

            if (distEnemyApple <= enemy!!.radius + apple!!.radius) {
                eventSubscriber.publish(Event.COLLISION, Collision.HEALS, Type.ENEMY)
                gameObjectController.removeGameObject(apple as GameObject)
                apple = null
                appleSpawn()
            }

            if (player.positionMap.x - player.radius < 0 || player.positionMap.x + player.radius > mapWidth) {
                gameEventListener?.onGameOver()
                pauseGame()
            }

            if (player.positionMap.y - player.radius < 0 || player.positionMap.y + player.radius > mapHeight) {
                gameEventListener?.onGameOver()
                pauseGame()
            }
        }
    }

    fun spawnEnemy() {
        val playerPart = getScreenPart(player.positionMap, mapWidth.toInt(), mapHeight.toInt())
        val newPosition = randomPart(playerPart, mapWidth.toInt(), mapHeight.toInt())
        val screenAndMapPos = gameObjectController.getScreenPositionAndMapPos(newPosition)
        enemy = Enemy(newPosition, screenAndMapPos.first, context)
        gameObjectController.addGameObject(enemy!!)
        eventSubscriber.subscribe(Event.COLLISION, enemy!!)
    }

    fun appleSpawn() {
        if (apple == null) {
            val playerPart = getScreenPart(player.positionMap, mapWidth.toInt(), mapHeight.toInt())
            val newPosition = randomPart(playerPart, mapWidth.toInt(), mapHeight.toInt())

            apple = gameObjectController.createGameObject<Apple>(newPosition)

            enemy?.setApplePos(apple!!.positionScreen)
        }
    }

    fun setGameEventListener(gameEventListener: GameEventListener) {
        this.gameEventListener = gameEventListener
    }

    @SuppressLint("NewApi")
    override fun update(event: Event, data: Any?, type: Type) {
        when (event) {
            Event.POSITION -> {
                val point = data as Point

                vecX = point.x * player.speed
                vecY = point.y * player.speed
            }

            Event.COLLISION -> {
                Log.e("GAME", "1x = ${player.moveHistory.x} y = ${player.moveHistory.y}")
                Log.e("GAME", "2x = ${player.positionMap.x} y = ${player.positionMap.y}")
                Log.e("GAME", "3x = ${travel.x} y = ${travel.y}")
                Log.e("GAME", "4x = ${player.positionMap.x} y = ${player.positionMap.y}")
                val bundle = Bundle()
                bundle.putInt("i", 1)
                gameObjectController.createGameObject<TestObject>(Point(100f, 100f), bundle)
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

    fun updateGameObjects() {
        travel.x -= vecX
        travel.y -= vecY
        player.updatePosition(Point(vecX / player.speed, vecY / player.speed))
        gameObjectController.update(vecX, vecY)
        // send updated position
        gameEventListener?.sendMyPosition(player.positionMap)
    }

    fun updateWalls() {
        walls.update(vecX, vecY)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawColor(Color.BLACK)

        updateGameObjects()

        paint.color = Color.WHITE
        canvas.drawCircle(ballX, ballY, ballRadius, paint)
        gameObjectController.draw(canvas)

        paint.color = Color.YELLOW
        rect.update(vecX, vecY)

        canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, paint)

        if (apple != null) apple!!.draw(canvas)

        //  Game arena
        updateWalls()
        walls.draw(canvas)

        //  UI
        paint.color = Color.WHITE
        paint.textSize = 40f
        canvas?.drawText("Score: ${player.getSize()}", width - 240f, 50f, paint)
    }
}