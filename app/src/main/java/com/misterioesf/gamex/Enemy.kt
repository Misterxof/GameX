package com.misterioesf.gamex

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.misterioesf.gamex.model.Collision
import com.misterioesf.gamex.model.Event
import com.misterioesf.gamex.model.GameObject
import com.misterioesf.gamex.model.Point
import com.misterioesf.gamex.model.Segment
import com.misterioesf.gamex.model.Type
import kotlin.math.sqrt

class Enemy(
    val startPos: Point,
    startScreenPos: Point,
    context: Context
) : GameObject(startScreenPos, context), Subscriber {
    override var type: Type = Type.PLAYER
    override var positionMap: Point

    var speed = 7f
    var radius = 50f
    var vecX = 0f
    var vecY = 0f
    val playerPath: PlayerPath
    var head: Segment
    var nextSegmentConst = 7
    var nextSegmentId = nextSegmentConst
    val segments = mutableListOf<Segment>()
    val moveHistory: Point
    val prevMoveHistory: Point
    val offsetPoint: Point
    var applePosition: Point? = null
    var color = Color.RED

    init {
        positionMap = startPos.copy()
        offsetPoint = Point(0f, 0f)
        offsetPoint.x = positionScreen.x - startPos.x
        offsetPoint.y = positionScreen.y - startPos.y
        moveHistory = Point(0f, 0f)
        prevMoveHistory = Point(0f, 0f)
        head = Segment(positionScreen, context)
        playerPath = PlayerPath(nextSegmentConst + 1, this.context)
    }

    fun getSize() = segments.size + 1

    fun getVectorX(): Float {
        return positionScreen.x
    }

    fun getVectorY(): Float {
        return positionScreen.y
    }

    fun move() {
        val pos = moveHistory.copy()
        pos -= prevMoveHistory
        //  add position offset from 0,0
        pos += positionScreen

        if (pos.x != positionScreen.x && pos.y != positionScreen.y) {
            playerPath.path.forEach {
                it.x += moveHistory.x - prevMoveHistory.x
                it.y += moveHistory.y - prevMoveHistory.y
            }
            playerPath.addCoordinate(pos.copy())
            head.update(positionScreen.copy())
        }

//        playerPath.addCoordinate(position.copy())
//
        segments.forEach {
            it.update(playerPath.path[it.i])
        }
    }

    fun addSegment() {
        if (((playerPath.path.lastIndex) / nextSegmentConst) > segments.size) {
            val newSegment = Segment(playerPath.path[nextSegmentId], context)
            newSegment.i = nextSegmentId
            segments.add(newSegment)
            playerPath.pathMaxSize += nextSegmentConst
            nextSegmentId += nextSegmentConst
        }
    }

    fun updatePosition(pair: Point) {
        vecX = pair.x
        vecY = pair.y
        positionMap.x += pair.x * speed
        positionMap.y += pair.y * speed
        prevMoveHistory.x = moveHistory.x
        prevMoveHistory.y = moveHistory.y
        moveHistory.x -= pair.x * speed
        moveHistory.y -= pair.y * speed

        positionScreen.x += pair.x * speed
        positionScreen.y += pair.y * speed

//        Log.e("EEMY", "Move ${moveHistory.x}, ${moveHistory.y}")
//        Log.e("PLAYER", "Move ${positionHistory.x + moveHistory.x}, ${positionHistory.y + moveHistory.y}")
    }

    fun getNewDirectionVector() {
        applePosition?.let {
            val dx = applePosition!!.x - head.position.x
            val dy = applePosition!!.y - head.position.y

            val normal = 1 / sqrt(dx * dx + dy * dy)

            val vx = dx * normal
            val vy = dy * normal

            updatePosition(Point(vx, vy))
        }
    }

    fun setApplePos(position: Point) {
        applePosition = position
    }

    override fun updatePosition(x: Float, y: Float) {
        positionScreen.x -= x
        positionScreen.y -= y

        if (x != 0f || y != 0f) {
            changeSegments(x, y)
        }

        getNewDirectionVector()
        Log.e("ENEMY", "Pos updadtette x $x y $y")
    }

    fun changeSegments(x: Float, y: Float) {
        playerPath.path.forEach {
            it.x -= x
            it.y -= y
        }
    }

    override fun update(event: Event, data: Any?, type: Type) {
        when (event) {
            Event.POSITION -> TODO()
            Event.COLLISION -> {
                if (type == Type.ENEMY) {
                    when (data as Collision) {
                        Collision.HEALS -> {
                            Log.e("ENEMY", "COLLISION ${data.toString()}")
                            addSegment()
                        }

                        Collision.HIT -> TODO()
                    }
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

//        getNewDirectionVector()
        move()  // lags if moved to update
        var paint = Paint()
        paint.color = color
        paint.style = Paint.Style.FILL

        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        canvas?.drawRect(
            getVectorX() - radius,
            getVectorY() - radius,
            getVectorX() + radius,
            getVectorY() + radius,
            paint
        )

        for (i in segments.lastIndex downTo 0) {
            segments[i].draw(canvas)
        }
        head.draw(canvas)
        playerPath.draw(canvas)
    }
}