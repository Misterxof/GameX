package com.misterioesf.gamex.model

import android.content.Context
import android.graphics.Canvas

class Walls(context: Context): GameObject(Point(0f, 0f), context) {
    override var type: Type = Type.WALL
    override var positionMap: Point = Point(0f, 0f)

    override fun updatePosition(x: Float, y: Float) {

    }

    private lateinit var topWall: Rect
    private lateinit var bottomWall: Rect
    private lateinit var leftWall: Rect
    private lateinit var rightWall: Rect

    fun initWalls(mapWidth: Float, mapHeight: Float, screenOffset: Point) {
        topWall = Rect(0f, 0f, mapWidth, 5f, screenOffset, context)
        bottomWall = Rect(0f, mapHeight, mapWidth, mapHeight - 5f, screenOffset, context)
        leftWall = Rect(0f, 0f, 5f, mapHeight, screenOffset, context)
        rightWall = Rect(mapWidth, 0f, mapWidth - 5f, mapHeight, screenOffset, context)
    }

    fun update(vecX: Float, vecY: Float) {
        topWall.update(vecX, vecY)
        bottomWall.update(vecX, vecY)
        leftWall.update(vecX, vecY)
        rightWall.update(vecX, vecY)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        topWall.draw(canvas)
        bottomWall.draw(canvas)
        leftWall.draw(canvas)
        rightWall.draw(canvas)
    }
}