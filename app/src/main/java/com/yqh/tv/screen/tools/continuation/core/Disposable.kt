package com.yqh.tv.screen.tools.continuation.core

import com.yqh.tv.screen.tools.continuation.Job
import com.yqh.tv.screen.tools.continuation.OnCancel
import com.yqh.tv.screen.tools.continuation.OnComplete

typealias OnCompleteT<T> = (Result<T>) -> Unit

interface Disposable {
    fun dispose()
}


sealed class DisposableList {
    object Nil : DisposableList()
    class Cons(val head: Disposable, val tail: DisposableList) : DisposableList()
}

/**
 * 在Job中添加 invokeOnCompletion 之后返回的 Disposable方法
 * 在结束自己的回调方法中进行移除自己的操作
 * 也就是说在协程结束的时候，就不需要在调用我自己了(因为已经被移除掉了)
 */
class CompletionHandlerDisposable<T>(val job: Job, val onComplete: OnCompleteT<T>) : Disposable {
    override fun dispose() {
        //调用的Job当中的remove方法 移除掉自己
        job.remove(this)
    }
}

/**
 * 在Job中添加 invokeOnCancellation 之后返回的 Disposable
 */
class CancellationHandlerDisposable(val job: Job, val onCancel: OnCancel) : Disposable {
    override fun dispose() {
        job.remove(this)
    }
}

/**
 * 移除某个元素
 * 返回的时移除掉了某个元素之后的递归列表
 */
fun DisposableList.remove(disposable: Disposable): DisposableList {
    return when (this) {
        DisposableList.Nil -> this
        is DisposableList.Cons -> {
            if (head == disposable) {
                return tail
            } else {
                DisposableList.Cons(head, tail.remove(disposable))
            }
        }
    }
}

/**
 * 循环递归列表
 */
tailrec fun DisposableList.forEach(action: (Disposable) -> Unit): Unit = when (this) {
    DisposableList.Nil -> Unit
    is DisposableList.Cons -> {
        action(this.head)
        this.tail.forEach(action)
    }
}

/**
 * 过滤递归列表中的 Disposable 类型，做处理
 */
inline fun <reified T : Disposable> DisposableList.loopOn(crossinline action: (T) -> Unit) =
    forEach {
        when (it) {
            is T -> action(it)
        }
    }
