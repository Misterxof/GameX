package com.misterioesf.gamex

import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceHolder
import java.util.concurrent.atomic.AtomicBoolean

class GameThread(private val surfaceHolder: SurfaceHolder, private val gameView: GameView) : Thread() {

    var running = AtomicBoolean(false)

    override fun run() {
        while (running.get()) {
            val canvas: Canvas? = surfaceHolder.lockCanvas()
            if (canvas != null) {
                if (gameView.apple == null)
                    gameView.appleSpawn()
                else gameView.collisionChecker()

                gameView.updateBall()
                gameView.draw(canvas)
                surfaceHolder.unlockCanvasAndPost(canvas)
            }

            try {
                sleep(16) // Adjust frame rate as needed
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun resumeThread() {
        running.set(true)
    }

    fun stopThread() {
        running.set(false   )
    }
}