package com.misterioesf.gamex

import com.misterioesf.gamex.model.Point
import kotlin.math.abs
import kotlin.math.sqrt

fun getDistance(p1: Point, p2: Point): Float {
    val powX = abs(p1.x - p2.x) * abs(p1.x - p2.x)
    val powY = abs(p1.y - p2.y) * abs(p1.y - p2.y)

    return sqrt(powX + powY)
}