package com.misterioesf.gamex.model

import android.content.Context
import android.view.View

abstract class GameObject(context: Context): View(context) {
    abstract var type: Type
    lateinit var position: Point

    abstract fun updatePosition(x: Float, y: Float)
}