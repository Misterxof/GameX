package com.misterioesf.gamex

import com.misterioesf.gamex.model.Event

interface Subscriber {
    fun update(event: Event, data: Any?)
}