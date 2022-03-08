package com.martmists.discryption.commons

import kotlin.concurrent.thread
import kotlin.reflect.KProperty

class LazyThreadedValue<T : Any>(private val provider: () -> T) {
    var value: T? = null
    private val _thread = thread(start=true) {
        value = provider()
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (_thread.isAlive) {
            _thread.join()
        }
        return value!!
    }
}
