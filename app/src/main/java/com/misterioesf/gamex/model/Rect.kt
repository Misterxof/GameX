package com.misterioesf.gamex.model

class Rect(var left: Float, var top: Float, var right: Float, var bottom: Float, val offset: Point) {

    init {
        left += offset.x
        right += offset.x
        top += offset.y
        bottom += offset.y
    }

    fun update(x: Float, y: Float) {
        left -= x
        right -= x
        top -= y
        bottom -= y
    }
}