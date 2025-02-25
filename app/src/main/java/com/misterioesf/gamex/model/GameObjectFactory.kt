package com.misterioesf.gamex.model

import android.content.Context

object GameObjectFactory {
    inline fun <reified T : GameObject> createGameObject(startPosition: Point, context: Context, id: Int = 0): T? {
        return when (T::class) {
            TestObject::class -> TestObject(startPosition, context, id) as T
            Apple::class -> Apple(startPosition, context) as T
            else -> null
        }
    }
}