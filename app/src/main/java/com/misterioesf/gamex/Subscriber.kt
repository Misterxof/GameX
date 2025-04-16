package com.misterioesf.gamex

import com.misterioesf.gamex.model.Event
import com.misterioesf.gamex.model.Type

interface Subscriber {
    fun update(event: Event, data: Any?, type: Type)
}