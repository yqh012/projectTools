package com.yqh.tools.network.base

import android.util.Log
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.yqh.tools.network.eneity.ApiEmptyResponse
import com.yqh.tools.network.eneity.ApiFailResponse
import com.yqh.tools.network.eneity.ApiResponse
import com.yqh.tools.network.eneity.ApiSuccessResponse
import kotlinx.coroutines.delay
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseRepository {

    companion object {
        const val HOST_ERROR = "UnknownHostError"
        const val HOST_MSG = "找不到主机，请稍后再试。"

        const val CONNECT_ERROR = "ConnectError"
        const val CONNECT_MSG = "网络链接出错，请稍后再试。"

        const val JSON_ERROR = "jsonError"
        const val JSON_MSG = "数据解析出错。"

        const val SOCKET_ERROR = "socketError"
        const val SOCKET_MSG = "链接出错，请稍后再试。"

        const val TIMEOUT_ERROR = "timeOutError"
        const val TIMEOUT_MSG = "网络链接超时，请稍后再试。"

        const val UNKNOWN_ERROR = "unknownError"
        const val UNKNOWN_MSG = "网络出错"
    }

    /**
     * 执行接口请求的入口
     */
    open suspend fun <T> executeHttp(block: suspend () -> ApiResponse<T>): ApiResponse<T> {
        runCatching {
            block.invoke()
        }.onSuccess { response ->
            return handleHttpOk(response)
        }.onFailure { exception ->
            return handlerHttpError(exception)
        }
        return ApiEmptyResponse()
    }

    private fun <T> handlerHttpError(exception: Throwable): ApiResponse<T> {
        var errorCode = ""
        var errorMsg = ""
        when (exception) {
            is UnknownHostException -> {
                errorCode = HOST_ERROR
                errorMsg = HOST_MSG
            }
            is ConnectException -> {
                errorCode = CONNECT_ERROR
                errorMsg = CONNECT_MSG
            }
            is JSONException,
            is JsonParseException,
            is JsonSyntaxException -> {
                errorCode = JSON_ERROR
                errorMsg = JSON_MSG
            }
            is SocketException -> {
                errorCode = SOCKET_ERROR
                errorMsg = SOCKET_MSG
            }
            is SocketTimeoutException -> {
                errorCode = TIMEOUT_ERROR
                errorMsg = TIMEOUT_MSG
            }
            else -> {
                errorCode = UNKNOWN_ERROR
                errorMsg = UNKNOWN_MSG
            }
        }
        return ApiFailResponse(errorCode, errorMsg, exception)
    }

    private fun <T> handleHttpOk(response: ApiResponse<T>): ApiResponse<T> {
        //如果code为成功状态，则判断数据是否为空,不为空再返回成功
        return if (response.isSuccess) getHttpSuccessResponse(response)
        else ApiFailResponse(response.code, response.message)
    }

    private fun <T> getHttpSuccessResponse(response: ApiResponse<T>): ApiResponse<T> {
        val data = response.data
        return if (data == null || ((data is List<*>) && (data as List<*>).isEmpty())) ApiEmptyResponse()
        else ApiSuccessResponse(data)
    }

}