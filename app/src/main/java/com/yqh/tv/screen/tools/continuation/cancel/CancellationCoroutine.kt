package com.yqh.tv.screen.tools.continuation.cancel

import android.annotation.SuppressLint
import com.yqh.tv.screen.tools.continuation.CancellationException
import com.yqh.tv.screen.tools.continuation.Job
import com.yqh.tv.screen.tools.continuation.OnCancel
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.intrinsics.intercepted
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn

class CancellationCoroutine<T>(private val continuation: Continuation<T>) :
    Continuation<T> by continuation {

    private val state = AtomicReference<CancelState>(CancelState.InComplete)

    private val cancelHandlers = CopyOnWriteArrayList<OnCancel>()

    val isCompleted: Boolean
        get() = state.get() is CancelState.Complete<*>

    val isActive: Boolean
        get() = state.get() == CancelState.InComplete

    @SuppressLint("NewApi")
    override fun resumeWith(result: Result<T>) {
        state.updateAndGet {
            when (it) {
                CancelState.InComplete -> {
                    continuation.resumeWith(result)
                    CancelState.Complete(result.getOrNull(), result.exceptionOrNull())
                }
                CancelState.Cancelled -> {
                     CancellationException("已经取消了").let {
                        continuation.resumeWith(Result.failure(it))
                        CancelState.Complete(null, it)
                    }
                }
                is CancelState.Complete<*> -> throw IllegalStateException("状态异常，当前已经完成了，不会在完成一次了。")
            }
        }
    }

    fun cancel() { //只有 isActive状态才去进行取消操作
        if (!isActive) return
        val parent = continuation.context[Job] ?: return
        //把父 Job 的协程也cancel掉
        parent.cancel()
    }

    fun invokeOnCancel(onCancel: OnCancel) {
        cancelHandlers + onCancel
    }

    fun getResult(): Any? {
        installCancelHandler()
        return when (val currentState = state.get()) {
            CancelState.InComplete -> COROUTINE_SUSPENDED
            CancelState.Cancelled -> {
                throw CancellationException("continuation 协程已经是 cancel 了")
            }
            is CancelState.Complete<*> -> {
                (currentState as CancelState.Complete<T>).let { it ->
                    it.exception?.let { throw it } ?: it.value
                }
            }
        }
    }

    /**
     * 监听协程取消的状态
     */
    private fun installCancelHandler() {
        if (!isActive) return
        val parent = continuation.context[Job] ?: return
        parent.invokeOnCancel {
            doCancel()
        }
    }

    @SuppressLint("NewApi")
    private fun doCancel() {
        state.updateAndGet {
            when (it) {
                CancelState.InComplete -> CancelState.Cancelled
                CancelState.Cancelled,
                is CancelState.Complete<*> -> it
            }
        }
        cancelHandlers.forEach(OnCancel::invoke)
        cancelHandlers.clear()
    }
}

suspend inline fun <T> suspendCancellableCoroutine(crossinline block: (CancellationCoroutine<T>) -> Unit): T =
    suspendCoroutineUninterceptedOrReturn { c: Continuation<T> ->
        val cancellationCoroutine = CancellationCoroutine(c.intercepted())
        block(cancellationCoroutine)
        cancellationCoroutine.getResult()
    }