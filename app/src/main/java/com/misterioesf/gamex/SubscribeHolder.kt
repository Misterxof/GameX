package com.misterioesf.gamex

import android.util.Log
import com.misterioesf.gamex.model.Event

class SubscribeHolder {
    private val subscribers: MutableMap<Event, MutableList<Subscriber>> = HashMap()

    fun subscribe(event: Event, subscriber: Subscriber) {
        subscribers.computeIfAbsent(event) { _: Event -> ArrayList() }.add(subscriber)
    }

    fun unsubscribe(event: Event, subscriber: Subscriber) {
        subscribers[event]?.remove(subscriber)
    }

    fun publish(event: Event, data: Any?) {
        val subs = subscribers[event]
        if (!subs.isNullOrEmpty()) {
            subs.forEach {
                it.update(event, data)
            }
        }
    }

    fun getSubsEvent(event: Event) = subscribers[event]
}