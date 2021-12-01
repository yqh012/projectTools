package com.yqh.tv.screen.tools.viewmodel

import androidx.lifecycle.ViewModel
import com.yqh.base.repository.repositoryOf
import com.yqh.tv.screen.tools.repository.UserRepository


class MainViewModel : ViewModel() {
    // 通过属性代理获取当前Repository对象，模拟注入事件
    private val userRepository: UserRepository by repositoryOf()

    fun userRepositorySee() {
        userRepository.seeHello()
    }

}