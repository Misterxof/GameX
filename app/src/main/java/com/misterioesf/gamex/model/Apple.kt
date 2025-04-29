package com.misterioesf.gamex.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import com.misterioesf.gamex.Subscriber
import com.misterioesf.gamex.collisions.Collidable

class Apple(startPosition: Point, context: Context): GameObject(startPosition, context, null), Subscriber {
    override var type: Type = Type.POWER
    override lateinit var positionMap: Point
    override val collisionType = Type.HEALS
    override val collisionRadius = 30f
    override var isCollidable = true
    override var gameObjects: MutableList<GameObject>? = null

    var radius = 30f
    val paint = Paint()

    override fun update(event: Event, data: Any?, type: Type) {
        when(event) {
            Event.POSITION -> TODO()
            Event.COLLISION -> Log.e("APPLE", "COLLISION ${data.toString()}")
        }
    }

    override fun updatePosition(x: Float, y: Float) {
        positionScreen.x -= x
        positionScreen.y -= y
    }

    override fun handleCollision(other: Collidable) {
        when (other.collisionType) {
            Type.ENEMY -> {
                Log.e("PLAYER", "COLLISION apple")
            }
            Type.POWER -> {

            }
            Type.WALL -> {

            }
            else -> {}
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        paint.color = Color.RED
        canvas?.drawCircle(positionScreen.x, positionScreen.y, radius, paint)
    }

    fun setPositionMapValue(position: Point) {
        positionMap = position.copy()
    }

    override fun toString(): String {
        return "Apple(type=$type, position x = ${positionScreen.x} y = ${positionScreen.y})"
    }

    companion object: Creator {
        override fun create(positionScreen: Point, context: Context, bundle: Bundle?): Apple {
            return Apple(positionScreen, context)
        }
    }
}