package com.misterioesf.gamex

import com.misterioesf.gamex.model.Point

interface GameEventListener {
    fun onGameOver()
    fun sendMyPosition(position: Point)
}