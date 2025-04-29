package com.misterioesf.gamex.model

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.view.View

class GameObjectController private constructor(val screenOffset: Point, context: Context): View(context) {
    private var gameObjects = mutableListOf<GameObject>()
    val screenMapPositionChange: Point = Point(0f, 0f)

    inline fun <reified T : GameObject> createGameObject(startPosition: Point, bundle: Bundle? = null): T? {
        val screenPosition = startPosition + screenOffset + screenMapPositionChange
        val mapPosition = startPosition + screenMapPositionChange
        val gameObject = GameObjectFactory.createGameObject<T>(screenPosition, context, bundle)

        gameObject?.let {
            gameObject.positionMap = mapPosition
            addGameObject(gameObject)
        }

        return gameObject
    }

    fun getScreenPositionAndMapPos(startPosition: Point): Pair<Point, Point> {
        val screenPosition = startPosition + screenOffset + screenMapPositionChange
        val mapPosition = startPosition + screenMapPositionChange
        return screenPosition to mapPosition
    }

    fun addGameObject(gameObject: GameObject) {
        gameObjects.add(gameObject)
    }

    fun removeGameObject(gameObject: GameObject) {
        gameObjects.remove(gameObject)
    }

    fun isGameObjectExist(type: Type): Boolean {
        gameObjects.forEach {
            if (it.type == type) return true
        }

        return false
    }

    fun update(x: Float, y: Float) {
        screenMapPositionChange.x -= x
        screenMapPositionChange.y -= y

        gameObjects.forEach {
            it.updatePosition(x, y)
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        gameObjects.forEach { view -> view.draw(canvas) }
    }

    fun getGameObjects(): MutableList<GameObject> {
        return gameObjects
    }

    companion object {
        private var instance: GameObjectController? = null

        fun getInstance(screenOffset: Point, context: Context): GameObjectController {
            if (instance == null)
                instance = GameObjectController(screenOffset, context)

            return instance!!
        }
    }
}