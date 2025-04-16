package com.misterioesf.gamex.model

import java.io.Serializable

data class Point(var x: Float, var y: Float): Serializable {
    operator fun plus(point: Point): Point {
        return Point(this.x + point.x, this.y + point.y)
    }

    operator fun minus(point: Point): Point {
        return Point(this.x - point.x, this.y - point.y)
    }

    operator fun plusAssign(point: Point) {
        this.x += point.x
        this.y += point.y
    }

    operator fun minusAssign(point: Point) {
        this.x -= point.x
        this.y -= point.y
    }

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

