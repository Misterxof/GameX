package com.misterioesf.gamex

interface MoveUpdateListener {
    fun onPositionUpdate(movementVector: Pair<Float, Float>)
}