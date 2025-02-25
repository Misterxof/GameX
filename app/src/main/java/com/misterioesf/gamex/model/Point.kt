package com.misterioesf.gamex.model

data class Point(var x: Float, var y: Float) {
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
}

