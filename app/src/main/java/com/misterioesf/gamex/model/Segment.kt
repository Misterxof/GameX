package com.misterioesf.gamex.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class Segment(startPos: Point, context: Context): View(context) {
    var posX = startPos.x
    var posY = startPos.y
    var radius = 50f
    var i = -1
    val paint = Paint()

    fun update(point: Point) {
        posX = point.x
        posY = point.y
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        // draw body
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL
        canvas?.drawCircle(posX, posY, radius, paint)

        //  draw stroke
        paint.color =  Color.RED
        paint.style = Paint.Style.STROKE
        canvas?.drawCircle(posX, posY, radius, paint)
    }
}