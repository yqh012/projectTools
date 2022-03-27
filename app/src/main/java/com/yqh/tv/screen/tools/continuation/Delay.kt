package com.yqh.tv.screen.tools.continuation

import android.util.Log
import com.yqh.tv.screen.tools.continuation.cancel.suspendCancellableCoroutine
import com.yqh.tv.screen.tools.continuation.util.log
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private val executors = Executors.newScheduledThreadPool(1) { runnable ->
    Thread(runnable, "Delayed-Scheduler").apply {
        isDaemon = true
    }
}

suspend fun delay(time: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS) =
//    suspendCoroutine<Unit> { continuation ->
    suspendCancellableCoroutine<Unit> { continuation ->
        val future = executors.schedule({
            log("delayed...")
            continuation.resume(Unit)
        }, time, timeUnit)
        continuation.invokeOnCancel { future.cancel(true) }
//        continuation.cancel()
    }