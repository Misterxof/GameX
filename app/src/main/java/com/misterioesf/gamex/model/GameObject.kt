package com.misterioesf.gamex.model

import android.content.Context
import android.view.View

abstract class GameObject(val positionScreen: Point, context: Context) : View(context) {
    abstract var type: Type
    abstract var positionMap: Point

    abstract fun updatePosition(x: Float, y: Float)
}