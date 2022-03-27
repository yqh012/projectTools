package com.yqh.tv.screen.tools.continuation.core

import android.annotation.SuppressLint
import com.yqh.tv.screen.tools.continuation.CancellationException
import com.yqh.tv.screen.tools.continuation.Job
import com.yqh.tv.screen.tools.continuation.OnCancel
import com.yqh.tv.screen.tools.continuation.OnComplete
import kotlinx.coroutines.CoroutineName
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

abstract class AbstractCoroutine<T>(context: CoroutineContext) : Job, Continuation<T> {

    protected val state = AtomicReference<CoroutineState>()

    override val context: CoroutineContext by lazy { context + this }

    init {
        state.set(CoroutineState.InComplete())
    }

    override val isActive: Boolean
        get() = state.get() is CoroutineState.InComplete

    override val isCompleted: Boolean
        get() = state.get() is CoroutineState.Complete<*>

    @SuppressLint("NewApi")
    override fun resumeWith(result: Result<T>) {
        val newState = state.updateAndGet { prev ->
            when (prev) {
                is CoroutineState.Cancelling, //如果是 Cancelling 状态，在协程结束时，跟 InComplete 状态处理方式一样
                is CoroutineState.InComplete -> {
                    CoroutineState.Complete(result.getOrNull(), result.exceptionOrNull()).from(prev)
                }
                is CoroutineState.Complete<*> -> throw IllegalStateException("协程已经结束了，不能在结束了...")
            }
        }
        //TODO
        (newState as? CoroutineState.Complete<T>)?.exception?.let(::tryHandleException)

        //协程结束回调的通知
        newState.notifyCompletion(result)
        newState.clear()
    }

    private fun tryHandleException(exception: Throwable): Boolean {
        return when (exception) {
            is CancellationException -> false
            else -> {
                handleJobExceptions(exception)
            }
        }
    }

    protected open fun handleJobExceptions(exception: Throwable): Boolean {
        return false
    }

    /**
     * 协程结束后的回调
     */
    override fun invokeOnCompletion(onComplete: OnComplete): Disposable {
        return doOnCompleted {
            onComplete.invoke()
        }
    }

    @SuppressLint("NewApi")
    override fun remove(disposable: Disposable) {
        state.updateAndGet { prev ->
            when (prev) {
                is CoroutineState.InComplete -> {
                    CoroutineState.InComplete().from(prev).withOut(disposable)
                }
                is CoroutineState.Cancelling ->
                    CoroutineState.Cancelling().from(prev).withOut(disposable)
                is CoroutineState.Complete<*> -> {
                    /**
                     * 如果状态是已经结束，表示协程已经结束了
                     * 所以直接返回原来的状态就好，不需要再做其他处理
                     */
                    prev
                }
            }
        }
    }

    override suspend fun join() {
        when (state.get()) {
            is CoroutineState.Cancelling,
            is CoroutineState.InComplete -> {
                //判断状态是否是未完成状态，如果是未完成状态，则让它真正的挂起
                return joinSuspend()
            }
            is CoroutineState.Complete<*> -> {
                //状态已经结束了，直接return
                return
            }

        }
    }

    private suspend fun joinSuspend() = suspendCoroutine<Unit> { continuation ->
        //如果挂起了，我们只需要在协程执行完了之后，在去调用它就可以了。
        doOnCompleted {
            // 此处的 continuation 并不会关注结果是什么，只会关注是否执行完成了。
            continuation.resume(Unit)
        }
    }

    @SuppressLint("NewApi")
    protected fun doOnCompleted(block: (Result<T>) -> Unit): Disposable {
        val disposable = CompletionHandlerDisposable(this, block)
        val newState = state.updateAndGet { prev ->
            when (prev) {
                is CoroutineState.InComplete -> {
                    /**
                     * 如果这个状态还没结束，就把创建的这个回调(** CompletionHandlerDisposable **)添加进去
                     * 返回一个新的state状态对象，并copy之前的回调递归列表
                     */
                    CoroutineState.InComplete().from(prev).with(disposable)
                }
                is CoroutineState.Cancelling -> //添加了一个disposable，所以更新状态后添加进去就ok了
                    CoroutineState.Cancelling().from(prev).with(disposable)
                is CoroutineState.Complete<*> -> {
                    /**
                     * 如果状态是已经结束了，就不需要做操作了，直接把状态原路返回就好
                     * 相当于 此方法在Complete状态之前就已经返回了
                     */
                    prev
                }
            }
        }

        (newState as? CoroutineState.Complete<T>)?.let {
            block.invoke(
                when {
                    it.value != null -> Result.success(it.value)
                    it.exception != null -> Result.failure(it.exception)
                    else -> throw IllegalStateException("协程完成之后的值只有成功或者异常，不会存在其他的状态..")
                }
            )
        }

        return disposable
    }

    @SuppressLint("NewApi")
    override fun invokeOnCancel(onCancel: OnCancel): Disposable {
        val disposable = CancellationHandlerDisposable(this, onCancel)
        val newState = state.updateAndGet {
            when (it) {
                is CoroutineState.InComplete ->
                    CoroutineState.InComplete().from(it).with(disposable)
                is CoroutineState.Cancelling,
                is CoroutineState.Complete<*> -> it
            }
        }

        /**
         * 如果状态是 Canceling 取消状态，就需要 onCancel 的block回调
         * 如果状态是 Complete 完成状态，代表已经结束了，就不需要在做其他操作了
         */
        (newState as? CoroutineState.Cancelling)?.let {
            onCancel.invoke()
//            it.notifyCancelling()
        }
        return disposable
    }

    @SuppressLint("NewApi")
    override fun cancel() {
        val newState = state.updateAndGet {
            when (it) {
                is CoroutineState.InComplete -> CoroutineState.Cancelling().from(it)
                is CoroutineState.Cancelling,
                is CoroutineState.Complete<*> -> it
            }
        }

        if (newState is CoroutineState.Cancelling) {
            /**
             * 如果是取消状态的话，就直接回调 cancel 的回调 block
             */
            newState.notifyCancelling()
        }
    }

    override fun toString(): String {
        return "${context[CoroutineName]?.name}"
    }
}