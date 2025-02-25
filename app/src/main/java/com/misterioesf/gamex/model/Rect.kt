package com.misterioesf.gamex.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class Rect(
    var left: Float,
    var top: Float,
    var right: Float,
    var bottom: Float,
    val offset: Point,
    context: Context
): View(context) {
    val paint = Paint()

    init {
        left += offset.x
        right += offset.x
        top += offset.y
        bottom += offset.y
        paint.color = Color.LTGRAY
    }

    fun update(x: Float, y: Float) {
        left -= x
        right -= x
        top -= y
        bottom -= y
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        canvas?.drawRect(left, top, right, bottom, paint)
    }
}