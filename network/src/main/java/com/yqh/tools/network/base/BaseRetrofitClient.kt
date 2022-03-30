package com.yqh.tools.network.base

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseRetrofitClient {

    abstract fun getOkHttpClient(): OkHttpClient

    open fun <Api> getApi(apiClass: Class<Api>, baseUrl: String): Api {
        return Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(apiClass)
    }

}