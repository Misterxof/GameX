package com.misterioesf.gamex

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View

class Player(context: Context) : View(context) {
    var posX = 500f
    var posY = 500f
    var speed = 7f
    var vecX = 0f
    var vecY = 0f

    fun getVectorX(): Float {
        return posX
    }

    fun getVectorY(): Float {
        return posY
    }

    fun move() {
        posX += vecX * speed
        posY += vecY * speed
    }

    fun update(pair: Pair<Float, Float>) {
        vecX = pair.first
        vecY = pair.second

        //invalidate()
       // Log.e("PLAYER", "x = $posX y = $posY mx = ${pair.first * speed} my = ${pair.second * speed}")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        move()
        var paint = Paint()
        paint.color = Color.GREEN

        canvas?.drawCircle(getVectorX(), getVectorY(), 50f, paint)
    }
}