package com.misterioesf.gamex.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.misterioesf.gamex.collisions.Collidable

class Segment(startPosition: Point, context: Context, type: Type, isCollidable: Boolean): GameObject(startPosition, context, null) {
    var radius = 50f
    var i = -1
    val paint = Paint()
    override var type: Type = type
    override lateinit var positionMap: Point
    override var isCollidable = isCollidable
    override var gameObjects: MutableList<GameObject>? = null

    override fun updatePosition(x: Float, y: Float) {}

    override fun handleCollision(other: Collidable) {
        Log.e("Enemy", "COLLISION SEGMENT 1 ${other.collisionType}")
    }

    override val collisionType = type
    override val collisionRadius = radius

    fun update(point: Point) {
        positionScreen.x = point.x
        positionScreen.y = point.y
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        // draw body
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL
        canvas?.drawCircle(positionScreen.x, positionScreen.y, radius, paint)

        //  draw stroke
        paint.color =  Color.RED
        paint.style = Paint.Style.STROKE
        canvas?.drawCircle(positionScreen.x, positionScreen.y, radius, paint)
    }

    override fun toString(): String {
        return "Segment(position=$position, radius=$radius, i=$i, paint=$paint)"
    }


}