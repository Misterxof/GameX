package com.misterioesf.gamex.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View
import com.misterioesf.gamex.Subscriber

class Apple(startPosition: Point, context: Context): GameObject(startPosition, context), Subscriber {
    override var type: Type = Type.POWER
    override lateinit var positionMap: Point

    var radius = 30f
    val paint = Paint()

    override fun update(event: Event, data: Any?) {
        when(event) {
            Event.POSITION -> TODO()
            Event.COLLISION -> Log.e("APPLE", "COLLISION ${data.toString()}")
        }
    }

    override fun updatePosition(x: Float, y: Float) {
        positionScreen.x -= x
        positionScreen.y -= y
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


}