package com.misterioesf.gamex

import android.util.Log

class SubscribeHolder {
    private val subscribers: MutableMap<String, MutableList<Subscriber>> = HashMap()

    fun subscribe(event: String, subscriber: Subscriber) {
        subscribers.computeIfAbsent(event) { _: String? -> ArrayList() }.add(subscriber)
    }

    fun unsubscribe(event: String, subscriber: Subscriber) {
        subscribers[event]?.remove(subscriber)
    }

    fun publish(event: String, data: Any?) {
        val subs = subscribers[event]
        if (!subs.isNullOrEmpty()) {
            subs.forEach {
                it.update(event, data)
            }
        }
    }
}