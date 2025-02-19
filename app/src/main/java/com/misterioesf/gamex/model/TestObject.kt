package com.misterioesf.gamex.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.icu.text.Transliterator

class TestObject(pos: Point, context: Context, val i: Int): GameObject(context) {
    override var type: Type = Type.NEUTRAL

    init {
        position = pos
    }

    override fun updatePosition(x: Float, y: Float) {
        position.x -= x
        position.y -= y
    }

    override fun onDraw(canvas: Canvas?) {
        val paint = Paint()
        if (i == 1){
            paint.color = Color.BLUE
            paint.style = Paint.Style.STROKE
            canvas?.drawCircle(position.x, position.y, 50f, paint)

        } else {
            paint.color = Color.WHITE
            canvas?.drawCircle(position.x, position.y, 50f, paint)
        }
    }
}