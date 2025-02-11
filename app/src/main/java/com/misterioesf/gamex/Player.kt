package com.misterioesf.gamex

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View
import com.misterioesf.gamex.model.Collision
import com.misterioesf.gamex.model.Event
import com.misterioesf.gamex.model.GameObject
import com.misterioesf.gamex.model.Point
import com.misterioesf.gamex.model.Segment
import com.misterioesf.gamex.model.Type
import kotlin.math.abs
import kotlin.math.sqrt

class Player(context: Context) : GameObject(context), Subscriber {
    override var type: Type = Type.PLAYER
    var speed = 7f
    var radius = 50f
    var vecX = 0f
    var vecY = 0f
    val playerPath: PlayerPath
    var head: Segment
    var nextSegmentConst = 14
    var nextSegmentId = nextSegmentConst
    val segments = mutableListOf<Segment>()

    init {
        position = Point(500f, 500f)
        head = Segment(position, context)
        playerPath = PlayerPath(nextSegmentConst + 1, this.context)
    }

    fun getVectorX(): Float {
        return position.x
    }

    fun getVectorY(): Float {
        return position.y
    }

    fun move() {
        position.x += vecX * speed
        position.y += vecY * speed
        head.update(position)
        playerPath.addCoordinate(position.copy())

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
    }

    override fun update(event: Event, data: Any?) {
        when(event) {
            Event.POSITION -> TODO()
            Event.COLLISION -> {
                when(data as Collision) {
                    Collision.HEALS -> {
                        Log.e("PLAYER", "COLLISION ${data.toString()}")
                        addSegment()
                    }
                    Collision.HIT -> TODO()
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        move()  // lags if moved to update
        var paint = Paint()
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL


        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        canvas?.drawRect(getVectorX() - radius, getVectorY() - radius, getVectorX() + radius, getVectorY() + radius, paint)

        for (i in segments.lastIndex downTo 0){
            segments[i].draw(canvas)
        }
        head.draw(canvas)
        playerPath.draw(canvas)
    }
}