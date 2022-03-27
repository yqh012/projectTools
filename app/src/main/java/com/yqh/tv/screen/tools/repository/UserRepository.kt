package com.yqh.tv.screen.tools.repository

import com.blankj.utilcode.util.LogUtils
import com.yqh.base.repository.AbsRepository
import com.yqh.tv.screen.libannotation.Repository

/**
 * 创建 Repository 业务实现类
 */
@Repository
class UserRepository : AbsRepository(){
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

fun main() {
}
