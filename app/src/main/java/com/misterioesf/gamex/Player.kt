package com.misterioesf.gamex

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View
import com.misterioesf.gamex.model.Point
import com.misterioesf.gamex.model.Segment
import kotlin.math.abs
import kotlin.math.sqrt

class Player(context: Context) : View(context) {
    var posX = 500f
    var posY = 500f
    var secPoint = Point(posX, posY)
    var speed = 7f
    var radius = 50f
    var vecX = 0f
    var vecY = 0f
    val playerPath: PlayerPath
    var head = Segment(Point(posX, posY),context)
    var nextSegmentConst = 14
    var nextSegmentId = nextSegmentConst
    val segments = mutableListOf<Segment>()
    lateinit var segment: Segment
    var flag = true

    init {
        playerPath = PlayerPath(160, this.context)
        segment = Segment(Point(posX, posY),context)
    }

    fun getVectorX(): Float {
        return posX
    }

    fun getVectorY(): Float {
        return posY
    }

    fun move() {
        posX += vecX * speed
        posY += vecY * speed
        head.update(Point(posX, posY))
        playerPath.addCoordinate(Point(posX, posY))

        if (segments.size < 10 && ((playerPath.path.lastIndex) / nextSegmentConst) > segments.size) {
            val newSegment = Segment(playerPath.path[nextSegmentId], context)
            newSegment.i = nextSegmentId
            segments.add(newSegment)
            Log.e("PLAYER","new segment $nextSegmentId")
            nextSegmentId += nextSegmentConst

        }

        segments.forEach {
            it.update(playerPath.path[it.i])
        }
    }

    fun getSecPoint(): Int{
        if (playerPath.path.isNotEmpty()){
            playerPath.path.forEachIndexed { index, p ->
              //  Log.e("PLAYER", " ${playerPath.path.size} ind $index ${getDistance(Point(posX, posY), p)}")
//                Log.e("PLAYER", " $$vecX $vecY")
//                Log.e("PLAYER", p.toString())
                if (getDistance(Point(posX, posY), p) >= radius * 2) {
                    secPoint = Point(p.x, p.y)

                    return playerPath.path.size
                }
            }
        }
        return -1
    }

    fun update(pair: Point) {
        vecX = pair.x
        vecY = pair.y
     //   move()
        //invalidate()
       // Log.e("PLAYER", "x = $posX y = $posY mx = ${pair.first * speed} my = ${pair.second * speed}")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        move()  // lags if moved to update
        var paint = Paint()
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL
        //canvas?.drawCircle(getVectorX(), getVectorY(), radius, paint)


        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        canvas?.drawRect(getVectorX() - radius, getVectorY() - radius, getVectorX() + radius, getVectorY() + radius, paint)
//        Log.e("PLAYER", "x = ${getVectorX() - radius / 2} y = ${getVectorY() - radius / 2} ")

        for (i in segments.lastIndex downTo 0){
            segments[i].draw(canvas)
        }
        head.draw(canvas)
        playerPath.draw(canvas)
    }
}