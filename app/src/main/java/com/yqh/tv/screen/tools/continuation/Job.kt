package com.yqh.tv.screen.tools.continuation

import com.yqh.tv.screen.tools.continuation.core.Disposable
import kotlinx.coroutines.CancellationException
import kotlin.coroutines.CoroutineContext

typealias OnComplete = () -> Unit
typealias OnCancel = () -> Unit
/**
 * 如果在取消了之后，挂起函数响应取消之后，没有正常的结果返回了
 * 此时抛出此异常就ok了
 */
typealias CancellationException = CancellationException

interface Job : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<Job>

    override val key: CoroutineContext.Key<*>
        get() = Job

    val isActive: Boolean

    val isCompleted: Boolean

    /**
     * job中的回调，结束时的回调
     */
    fun invokeOnCompletion(onComplete: OnComplete): Disposable

    fun invokeOnCancel(onCancel: OnCancel): Disposable

    fun remove(disposable: Disposable)

    suspend fun join()

    fun cancel()

}