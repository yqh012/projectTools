package com.yqh.tv.screen.tools.continuation.core

sealed class CoroutineState {
    /**
     * 此处的递归列表是 Job 中 invokeOnCompletion 方法的回调的集合，在状态转移的时候会带在其中
     */
    private var disposableList: DisposableList = DisposableList.Nil

    /**
     * 复制状态中的 disposableList 状态列表并返回一个带有之前状态列表的 CoroutineState
     */
    fun from(state: CoroutineState): CoroutineState {
        this.disposableList = state.disposableList
        return this
    }

    /**
     * 添加状态列表到新列表中，并返回状态
     */
    fun with(disposable: Disposable): CoroutineState {
        this.disposableList = DisposableList.Cons(disposable, this.disposableList)
        return this
    }

    /**
     * 从disposablelist中移除一个disposable，并返回状态
     */
    fun withOut(disposable: Disposable): CoroutineState {
        this.disposableList = this.disposableList.remove(disposable)
        return this
    }

    /**
     * 协程状态完成更新回调
     */
    fun <T> notifyCompletion(result: Result<T>) {
        this.disposableList.loopOn<CompletionHandlerDisposable<T>> {
            it.onComplete.invoke(result)
        }
    }

    /**
     * 协程取消后的更新回调
     */
    fun notifyCancelling() {
        this.disposableList.loopOn<CancellationHandlerDisposable> {
            it.onCancel.invoke()
        }
    }


    fun clear() {
        this.disposableList = DisposableList.Nil
    }

    class InComplete : CoroutineState()
    class Cancelling : CoroutineState()
    class Complete<T>(val value: T? = null, val exception: Throwable? = null) : CoroutineState()
}