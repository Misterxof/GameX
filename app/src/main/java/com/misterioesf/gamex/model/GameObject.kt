package com.misterioesf.gamex.model

import android.content.Context
import android.view.View
import com.misterioesf.gamex.collisions.Collidable

abstract class GameObject(
    val positionScreen: Point,
    context: Context,
    onNewGameObject: ((gameObject: GameObject) -> Unit)?
) : View(context), Collidable {
    override val position = positionScreen
    abstract var type: Type
    abstract var positionMap: Point
    abstract var gameObjects: MutableList<GameObject>?

    abstract fun updatePosition(x: Float, y: Float)
    abstract override fun handleCollision(other: Collidable)
}