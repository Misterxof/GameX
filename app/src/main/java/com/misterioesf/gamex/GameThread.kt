package com.misterioesf.gamex

import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceHolder

class GameThread(private val surfaceHolder: SurfaceHolder, private val gameView: GameView) : Thread() {

    var running = false

    override fun run() {
        while (running) {
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
}