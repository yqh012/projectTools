package com.yqh.tv.screen.tools.net

import com.yqh.tools.network.base.BaseRetrofitClient
import com.yqh.tv.screen.tools.net.api.ApiService
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object RetrofitClient : BaseRetrofitClient() {
    const val times = 10L

    val services by lazy { getApi(ApiService::class.java,ApiService.BASE_URL) }

    override fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(times, TimeUnit.SECONDS)
            .writeTimeout(times, TimeUnit.SECONDS)
            .connectTimeout(times, TimeUnit.SECONDS)
            .build()
    }
}