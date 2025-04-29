package com.misterioesf.gamex.collisions

import com.misterioesf.gamex.model.Point
import com.misterioesf.gamex.model.Type

interface Collidable {
    val collisionType: Type
    val position: Point
    val collisionRadius: Float
    var isCollidable: Boolean
    fun handleCollision(other: Collidable)
}