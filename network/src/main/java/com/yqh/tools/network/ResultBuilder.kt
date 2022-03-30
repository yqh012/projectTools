package com.yqh.tools.network

import com.yqh.tools.network.eneity.ApiEmptyResponse
import com.yqh.tools.network.eneity.ApiFailResponse
import com.yqh.tools.network.eneity.ApiResponse
import com.yqh.tools.network.eneity.ApiSuccessResponse

/**
 * 处理所有接口请求的回调
 */
fun <T> ApiResponse<T>.parseData(builder: ResultBuilder<T>.() -> Unit) {
    val listener = ResultBuilder<T>().also(builder)
    when (this) {
        is ApiSuccessResponse -> listener.onSuccess.invoke(response)
        is ApiEmptyResponse -> listener.onEmpty.invoke()
        is ApiFailResponse -> listener.onFailed.invoke(code, message, error)
    }
    listener.onComplete.invoke()
}

class ResultBuilder<T> {
    var onSuccess: (data: T?) -> Unit = {}
    var onEmpty: () -> Unit = {}
    var onFailed: (errorCode: String?, errorMsg: String?, exception: Throwable?) -> Unit =
        { _, _, _ -> }
    var onComplete: () -> Unit = {}
}