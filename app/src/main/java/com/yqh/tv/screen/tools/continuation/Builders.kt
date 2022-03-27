package com.yqh.tv.screen.tools.continuation

import com.yqh.tv.screen.tools.continuation.common.BlockingCoroutine
import com.yqh.tv.screen.tools.continuation.common.BlockingQueueDispatcher
import com.yqh.tv.screen.tools.continuation.common.DispatcherContext
import com.yqh.tv.screen.tools.continuation.common.Dispatchers
import com.yqh.tv.screen.tools.continuation.core.DeferredCoroutine
import com.yqh.tv.screen.tools.continuation.core.StandardCoroutine
import kotlinx.coroutines.CoroutineName
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

private var coroutineIndex = AtomicInteger(0)

fun launch(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend () -> Unit
): Job {
    //协程执行完成的 completion Continuation
    val completion = StandardCoroutine<Unit>(newCoroutineContext(context))
    block.startCoroutine(completion)
    return completion
}

fun <T> async(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend () -> T
): Deferred<T> {
    val completion = DeferredCoroutine<T>(newCoroutineContext(context))
    block.startCoroutine(completion)
    return completion
}

/**
 * 在协程中会判断当前协程中传递进来的 CoroutineContext中是否有调度器(拦截器)
 * 如果没有的话会自动添加一个。
 * 也就是通过这个函数(newCoroutineContext)来添加的
 */
fun newCoroutineContext(context: CoroutineContext): CoroutineContext {
    val combined = context + CoroutineName("@coroutine#${coroutineIndex.getAndIncrement()}")
    //如果这个 context 既不是 默认的拦截器，并且里面也没有拦截器，那么就给它添加一个默认的拦截器，如果有，就返回原来的
    return if (combined != Dispatchers.Default && combined[ContinuationInterceptor] == null)
        combined + Dispatchers.Default else combined
}

fun <T> runBlocking(context: CoroutineContext = EmptyCoroutineContext, block: suspend () -> T): T {
    val eventQueue = BlockingQueueDispatcher()
    val newContext = newCoroutineContext(context + DispatcherContext(eventQueue))
    val completion = BlockingCoroutine<T>(newContext, eventQueue)
    block.startCoroutine(completion)
    return completion.joinBlock()
}