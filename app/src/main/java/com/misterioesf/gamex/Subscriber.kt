package com.misterioesf.gamex

interface Subscriber {
    fun update(event: String?, data: Any?)
}