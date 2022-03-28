package com.yqh.tv.screen.tools.exception

import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.CoroutineContext.Key

class GlobalCoroutineExceptionHandler : CoroutineExceptionHandler {
    override val key: Key<*> = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        LogUtils.e(exception, "Unhandled Coroutine Exception with ${context[Job]}")
    }
}