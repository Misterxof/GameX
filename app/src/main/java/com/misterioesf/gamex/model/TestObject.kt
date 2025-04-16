package com.misterioesf.gamex.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.icu.text.Transliterator
import android.os.Bundle

class TestObject(pos: Point, context: Context, val i: Int): GameObject(pos, context) {
    override var type: Type = Type.NEUTRAL
    override lateinit var positionMap: Point

    override fun updatePosition(x: Float, y: Float) {
        positionScreen.x -= x
        positionScreen.y -= y
    }

    fun setPositionMapValue(position: Point) {
        positionMap = position.copy()
    }

    override fun onDraw(canvas: Canvas?) {
        val paint = Paint()
        if (i == 1){
            paint.color = Color.BLUE
            paint.style = Paint.Style.STROKE
            canvas?.drawCircle(positionScreen.x, positionScreen.y, 50f, paint)

        } else {
            paint.color = Color.WHITE
            canvas?.drawCircle(positionScreen.x, positionScreen.y, 50f, paint)
        }
    }


    companion object : Creator{
        override fun create(positionScreen: Point, context: Context, bundle: Bundle?): TestObject {
            val i = bundle?.getInt("i") ?: 0
            return TestObject(positionScreen, context, i)
        }
    }
}