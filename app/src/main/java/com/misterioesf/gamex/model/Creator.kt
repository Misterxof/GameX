package com.misterioesf.gamex.model

import android.content.Context
import android.os.Bundle

interface Creator {
    fun create(positionScreen: Point, context: Context, bundle: Bundle?): GameObject {
        throw UnsupportedOperationException("Factory method must be implemented in companion object")
    }
}