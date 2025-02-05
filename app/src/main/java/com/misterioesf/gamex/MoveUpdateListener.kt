package com.misterioesf.gamex

import com.misterioesf.gamex.model.Point

interface MoveUpdateListener {
    fun onPositionUpdate(movementVector: Point)
}