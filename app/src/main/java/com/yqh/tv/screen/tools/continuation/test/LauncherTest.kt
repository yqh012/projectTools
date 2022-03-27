package com.yqh.tv.screen.tools.continuation.test

import com.yqh.tv.screen.tools.continuation.Job
import com.yqh.tv.screen.tools.continuation.delay
import com.yqh.tv.screen.tools.continuation.exception.CoroutineExceptionHandler
import com.yqh.tv.screen.tools.continuation.launch
import com.yqh.tv.screen.tools.continuation.util.log
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun main() {
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        log(coroutineContext[Job], throwable)
    }

    val job = launch(exceptionHandler) {
        log(1)
        val result = hello()
        log(2, result)
        delay(1000)
        throw ArithmeticException("Div 0")
        log(3)
        log(Runtime.getRuntime().availableProcessors())
    }
    log(job.isActive)
//    job.cancel()
    log(job.isActive)
    job.join()

}

suspend fun hello() = suspendCoroutine<Int> {
    thread(isDaemon = true) {
        Thread.sleep(1000)
        it.resume(10086)
    }
}