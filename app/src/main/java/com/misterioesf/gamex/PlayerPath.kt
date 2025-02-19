package com.misterioesf.gamex

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.misterioesf.gamex.model.Point

class PlayerPath(var pathMaxSize: Int, context: Context) : View(context) {
    val path = ArrayDeque<Point>(pathMaxSize)
    var paint = Paint()
    var current: Point? = null
    var prev: Point? = null

    fun addCoordinate(point: Point) {
        prev = current
        current = point
        if (prev != null && current != null && prev != current) {
            if (pathMaxSize == path.size)
                path.removeLast()

            path.addFirst(point)
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        canvas?.drawRect(100f, 100f, 200f, 200f, paint)
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        if (path.isNotEmpty() && path.size > 1) {
            var prevs = path[0]
            for (i in 1..path.lastIndex - 1) {
                if (path[i] != null) {
                    canvas?.drawLine(prevs.x, prevs.y, path[i].x, path[i].y, paint)
                    prevs = Point(path[i].x, path[i].y)
                }
            }
            canvas?.drawLine(prev!!.x, prev!!.y, current!!.x, current!!.y, paint)
        }
    }
}