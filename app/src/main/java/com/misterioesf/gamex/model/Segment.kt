package com.misterioesf.gamex.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View

class Segment(startPos: Point, context: Context): View(context) {
    var position = startPos
    var radius = 50f
    var i = -1
    val paint = Paint()

    fun update(point: Point) {
        position = point
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        // draw body
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL
        canvas?.drawCircle(position.x, position.y, radius, paint)

        //  draw stroke
        paint.color =  Color.RED
        paint.style = Paint.Style.STROKE
        canvas?.drawCircle(position.x, position.y, radius, paint)
    }

    override fun toString(): String {
        return "Segment(position=$position, radius=$radius, i=$i, paint=$paint)"
    }


}