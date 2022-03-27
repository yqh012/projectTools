package com.yqh.tv.screen.tools.continuation.common

object Dispatchers {
    val Default by lazy {
        DispatcherContext(DefaultDispatcher)
    }
}