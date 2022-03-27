package com.yqh.tv.screen.tools.continuation.common

import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

object DefaultDispatcher : Dispatcher {
    private val threadGroup = ThreadGroup("DefaultDispatcher")
    private val threadIndex = AtomicInteger(0)
    private val executor =
        Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors()) {
            Thread(
                threadGroup,
                it,
                "${threadGroup.name}-worker-${threadIndex.getAndIncrement()}"
            ).apply {
                isDaemon = true
            }
        }

    override fun dispatch(block: () -> Unit) {
        executor.submit(block)
    }

}