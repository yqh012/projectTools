package com.yqh.tv.screen.tools.net.api

import com.aisier.bean.User
import com.aisier.bean.WxArticleBean
import com.yqh.tools.network.eneity.ApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    companion object {
        val BASE_URL = "https://wanandroid.com/"
    }


    @GET("wxarticle/chapters/json")
    suspend fun getWxArticle(): ApiResponse<List<WxArticleBean>>

    @GET("abc/chapters/json")
    suspend fun getWxArticleError(): ApiResponse<List<WxArticleBean>>

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") userName: String,
        @Field("password") passWord: String
    ): ApiResponse<User?>

}