package com.yqh.tv.screen.tools.continuation.common

import com.yqh.tv.screen.tools.continuation.core.AbstractCoroutine
import com.yqh.tv.screen.tools.continuation.core.CoroutineState
import java.util.concurrent.LinkedBlockingDeque
import kotlin.coroutines.CoroutineContext

typealias EventTask = () -> Unit

class BlockingQueueDispatcher : LinkedBlockingDeque<EventTask>(), Dispatcher {
    override fun dispatch(block: () -> Unit) {
        offer(block)
    }
}

class BlockingCoroutine<T>(
    context: CoroutineContext,
    private val queue: BlockingQueueDispatcher
) : AbstractCoroutine<T>(context) {
    fun joinBlock(): T {
        while (!isCompleted) {
            queue.take().invoke()
        }
        return (state.get() as CoroutineState.Complete<T>).let {
            it.value ?: throw it.exception!!
        }
    }
}