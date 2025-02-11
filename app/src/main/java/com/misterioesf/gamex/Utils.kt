package com.misterioesf.gamex

import com.misterioesf.gamex.model.Point
import com.misterioesf.gamex.model.ScreenPart
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

fun getDistance(p1: Point, p2: Point): Float {
    val powX = abs(p1.x - p2.x) * abs(p1.x - p2.x)
    val powY = abs(p1.y - p2.y) * abs(p1.y - p2.y)

    return sqrt(powX + powY)
}

fun getScreenPart(position: Point, width: Int, height: Int): ScreenPart {
    when {
        0 < position.x && position.x <= width / 2 &&
                0 < position.y && position.y <= height / 2 -> return ScreenPart.LEFT_TOP
        width / 2 < position.x && position.x <= width &&
                0 < position.y && position.y <= height / 2 -> return ScreenPart.RIGHT_TOP
        0 < position.x && position.x <= width / 2 &&
                height / 2 < position.y && position.y <= height -> return ScreenPart.LEFT_BOTTOM
        width / 2 < position.x && position.x <= width &&
                height / 2 < position.y && position.y <= height -> return ScreenPart.RIGHT_TOP
    }
    return ScreenPart.LEFT_TOP
}

fun randomPart(screenPart: ScreenPart, width: Int, height: Int): Point {
    val r = Random.nextInt(3)

    when(screenPart) {
        ScreenPart.LEFT_TOP -> {
            return randomPosition(
                ScreenPart.entries.filter { it != ScreenPart.LEFT_TOP }[r],
                width,
                height)
        }
        ScreenPart.RIGHT_TOP -> {
            return randomPosition(
                ScreenPart.entries.filter { it != ScreenPart.RIGHT_TOP }[r],
                width,
                height)
        }
        ScreenPart.LEFT_BOTTOM -> {
            return randomPosition(
                ScreenPart.entries.filter { it != ScreenPart.LEFT_BOTTOM }[r],
                width,
                height)
        }
        ScreenPart.RIGHT_BOTTOM -> {
            return randomPosition(
                ScreenPart.entries.filter { it != ScreenPart.RIGHT_BOTTOM }[r],
                width,
                height)
        }
    }
}

fun randomPosition(screenPart: ScreenPart, width: Int, height: Int): Point {
    var x = 0
    var y = 0

    when(screenPart) {
        ScreenPart.LEFT_TOP -> {
            x = Random.nextInt(50, width / 2 - 50)
            y = Random.nextInt(50, height / 2 - 50)
        }
        ScreenPart.RIGHT_TOP -> {
            x = Random.nextInt(width / 2 - 50, width - 50)
            y = Random.nextInt(50, height / 2 - 50)
        }
        ScreenPart.LEFT_BOTTOM -> {
            x = Random.nextInt(50, width / 2 - 50)
            y = Random.nextInt(height / 2 - 50, height - 50)
        }
        ScreenPart.RIGHT_BOTTOM -> {
            x = Random.nextInt(width / 2 - 50, width - 50)
            y = Random.nextInt(height / 2 - 50, height - 50)
        }
    }

    return Point(x.toFloat(), y.toFloat())
}