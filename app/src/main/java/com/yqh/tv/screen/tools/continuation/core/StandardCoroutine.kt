package com.yqh.tv.screen.tools.continuation.core

import com.yqh.tv.screen.tools.continuation.exception.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

class StandardCoroutine<T>(context: CoroutineContext) : AbstractCoroutine<T>(context) {

    override fun handleJobExceptions(exception: Throwable): Boolean {
        context[CoroutineExceptionHandler]?.handleException(context, exception)
            ?: Thread.currentThread().let {
                it.uncaughtExceptionHandler.uncaughtException(it, exception)
            }
        return true
    }
}