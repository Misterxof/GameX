package com.misterioesf.gamex

import android.content.Context
import android.util.Log
import com.misterioesf.gamex.model.Collision
import com.misterioesf.gamex.model.Event
import com.misterioesf.gamex.model.Point
import com.misterioesf.gamex.model.Type
import kotlin.math.sqrt

class Enemy(
    startPos: Point,
    startScreenPos: Point,
    nextSegmentConst: Int,
    context: Context
) : Player(startPos, startScreenPos, nextSegmentConst, context) {
    var applePosition: Point? = null

    override fun updatePosition(pair: Point) {
        super.updatePosition(pair)
        positionScreen.x += pair.x * speed
        positionScreen.y += pair.y * speed
    }

    // vector direction to apple
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

    override fun move() {
        super.move()
        head.update(positionScreen.copy())
    }

    // update from GameObjectController
    override fun updatePosition(x: Float, y: Float) {
        positionScreen.x -= x
        positionScreen.y -= y

        if (x != 0f || y != 0f) {
            changeSegments(x, y)
        }

        getNewDirectionVector()
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
}