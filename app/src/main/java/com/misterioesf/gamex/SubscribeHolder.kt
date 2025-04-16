package com.misterioesf.gamex

import android.util.Log
import com.misterioesf.gamex.model.Event
import com.misterioesf.gamex.model.Type

class SubscribeHolder {
    private val subscribers: MutableMap<Event, MutableList<Subscriber>> = HashMap()

    fun subscribe(event: Event, subscriber: Subscriber) {
        subscribers.computeIfAbsent(event) { _: Event -> ArrayList() }.add(subscriber)
    }

    fun unsubscribe(event: Event, subscriber: Subscriber) {
        subscribers[event]?.remove(subscriber)
    }

    fun publish(event: Event, data: Any?, type: Type) {
        val subs = subscribers[event]
        if (!subs.isNullOrEmpty()) {
            subs.forEach {
                it.update(event, data, type)
            }
        }
    }

    fun getSubsEvent(event: Event) = subscribers[event]
}