package com.yqh.tools.network.eneity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class ApiResponse<T>(
    open val data: T? = null,
    @SerializedName("errorCode")
    open val code: String? = null,
    @SerializedName("errorMsg")
    open val message: String? = null,
    open val error: Throwable? = null
) : Serializable {
    val isSuccess: Boolean
        get() = code == "0"

    override fun toString(): String {
        return "ApiResponse(data=$data, code=$code, message=$message, error=$error)"
    }
}

data class ApiSuccessResponse<T>(val response: T) : ApiResponse<T>(data = response)

class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiFailResponse<T>(
    override val code: String?, override val message: String?,
    override val error: Throwable? = null
) : ApiResponse<T>(code = code, message = message, error = error)

