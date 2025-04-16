package com.misterioesf.gamex.model

import android.content.Context
import android.os.Bundle

object GameObjectFactory {
    inline fun <reified T : GameObject> createGameObject(startPosition: Point, context: Context, bundle: Bundle? = null): T? {
        return when (T::class) {
            TestObject::class -> TestObject.create(startPosition, context, bundle) as T
            Apple::class -> Apple.create(startPosition, context, bundle) as T
            else -> null
        }
    }
}