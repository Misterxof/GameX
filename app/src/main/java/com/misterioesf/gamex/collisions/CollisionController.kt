package com.misterioesf.gamex.collisions

import android.util.Log
import com.misterioesf.gamex.getDistance
import com.misterioesf.gamex.model.GameObjectController
import com.misterioesf.gamex.model.Point
import kotlin.math.pow
import kotlin.math.sqrt

class CollisionController(private val gameObjectController: GameObjectController) {
    fun checkAllCollisions() {
        val objects = gameObjectController.getGameObjects()

        for (i in 0 until objects.size) {
            for (j in i + 1 until objects.size) {
                val obj1 = objects[i] as? Collidable
                val obj2 = objects[j] as? Collidable

                if (obj1 != null && obj2 != null && obj1.isCollidable && obj2.isCollidable &&
                    obj1.collisionType != obj2.collisionType &&
                    checkCollision(obj1, obj2)) {
                    Log.e("COLLISISON", "${obj1.collisionType} and ${obj2.collisionType}")
                    obj1.handleCollision(obj2)
                    obj2.handleCollision(obj1)
                }
            }
        }
    }

    private fun checkCollision(a: Collidable, b: Collidable): Boolean {
        val distance = getDistance(a.position, b.position)
        return distance <= (a.collisionRadius + b.collisionRadius)
    }
}