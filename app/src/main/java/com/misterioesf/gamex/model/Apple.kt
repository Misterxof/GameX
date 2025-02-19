package com.misterioesf.gamex.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View
import com.misterioesf.gamex.Subscriber

class Apple(startPosition: Point, context: Context): GameObject(context), Subscriber {
    override var type: Type = Type.POWER

    var radius = 30f
    val paint = Paint()

    init {
        position = startPosition
    }

    override fun update(event: Event, data: Any?) {
        when(event) {
            Event.POSITION -> TODO()
            Event.COLLISION -> Log.e("APPLE", "COLLISION ${data.toString()}")
        }
    }

    override fun updatePosition(x: Float, y: Float) {
        position.x -= x
        position.y -= y
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        paint.color = Color.RED
        canvas?.drawCircle(position.x, position.y, radius, paint)
    }

    override fun toString(): String {
        return "Apple(type=$type, position x = ${position.x} y = ${position.y})"
    }


}