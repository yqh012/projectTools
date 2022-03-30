package com.yqh.tv.screen.tools.repository

import com.blankj.utilcode.util.LogUtils
import com.yqh.base.repository.AbsRepository
import com.yqh.tv.screen.libannotation.Repository
import com.yqh.tv.screen.tools.net.RetrofitClient

/**
 * 创建 Repository 业务实现类
 */
@Repository
class UserRepository : AbsRepository() {
    init {
        LogUtils.i("UserRepository init ....")
    }

    fun seeHello() {
        println("hello")
    }
}

@Repository
class StudentRepository : AbsRepository() {
    init {
        LogUtils.i("StudentRepository init ....")
    }

    fun seePlay() {
        println("玩玩玩")
    }
}

@Repository
class WxArticleRepository : AbsRepository() {

    suspend fun getArticleInfo() =
        executeHttp {
            RetrofitClient.services.getWxArticle()
        }

}

fun main() {
}
