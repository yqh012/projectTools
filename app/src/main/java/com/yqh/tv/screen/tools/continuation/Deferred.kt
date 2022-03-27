package com.yqh.tv.screen.tools.continuation

interface Deferred<T> : Job {
    suspend fun await(): T
}