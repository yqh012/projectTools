package com.yqh.base.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yqh.base.view.IView
import com.yqh.tools.network.ResultBuilder
import com.yqh.tools.network.eneity.ApiResponse
import com.yqh.tools.network.parseData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private fun <T> launchFlow(
    block: suspend () -> ApiResponse<T>,
    startBlock: (() -> Unit)? = null,
    complete: (() -> Unit)? = null
) = flow { emit(block()) }.onStart { startBlock?.invoke() }.onCompletion { exception ->
    complete?.invoke()
}

/************************************************************************/

/**
 * 链式调用方式请求，不依赖 IView的 scope作用域，自己实现作用域
 */
suspend fun <T> launchScopeResult(
    block: suspend () -> ApiResponse<T>,
    builder: ResultBuilder<T>.() -> Unit,
    startBlock: (() -> Unit)?,
    complete: (() -> Unit)?
) {
    launchFlow(block, startBlock, complete).collect {
        it.parseData(builder)
    }
}

/************************************************************************/

/**
 * 链式调用方式的请求，结果直接在方法体内部返回，不需要创建订阅的方式
 */
fun <T> IView.launchAndResult(
    block: suspend () -> ApiResponse<T>,
    builder: ResultBuilder<T>.() -> Unit,
    startBlock: (() -> Unit)? = null,
    complete: (() -> Unit)? = null
) {
    lifecycleScope.launch {
        launchFlow(block, startBlock, complete).collect {
            it.parseData(builder)
        }
    }
}


/************************************************************************/

/**
 * 通过 IView 的lifecycleScope启动协程进行网络请求，与 launchSubscribe配套使用
 * startBlock : 启动请求的时候可以展示loading或其他，不传则使用默认逻辑
 * complete : 结束请求的时候可以取消loading或其他，不传则使用默认逻辑
 * tag : 用于在 launchSubscribe IView中 对 loading 及 hidden 做相关逻辑判断
 */
fun IView.launch(
    block: suspend () -> Unit,
    startBlock: (() -> Unit)? = null,
    complete: (() -> Unit)? = null,
    tag: String? = null
) {
    lifecycleScope.launch {
        flow {
            emit(block.invoke())
        }.onStart {
            startBlock?.invoke() ?: showLoading(tag)
        }.onCompletion { cause: Throwable? ->
            complete?.invoke() ?: dismissLoading(tag)
        }.collect()
    }
}

/**
 * 处理订阅回调的请求方式
 */
fun <T> Flow<ApiResponse<T>>.launchSubscribe(
    owner: LifecycleOwner,
    minActivityState: Lifecycle.State,
    listenerBuilder: ResultBuilder<T>.() -> Unit
) {
    if (owner is Fragment) {
        owner.viewLifecycleOwner.lifecycleScope.launch {
            owner.viewLifecycleOwner.repeatOnLifecycle(minActivityState) {
                collect { it.parseData(listenerBuilder) }
            }
        }
    } else {
        owner.lifecycleScope.launch {
            owner.repeatOnLifecycle(minActivityState) {
                collect { it.parseData(listenerBuilder) }
            }
        }
    }

    /************************************************************************/
}

