package com.yqh.tv.screen.tools.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.yqh.base.SuperActivity
import com.yqh.base.delegate.releasableNotNull
import com.yqh.base.delegate.release
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

abstract class BaseActivity<T : ViewBinding>(val inflater: (inflater: LayoutInflater) -> T) :
    SuperActivity() {

    /**
     * 属性代理不可为空的可释放类型，简便主程序业务代码的实现
     * @see(NullableDelegate)
     */
    protected var viewBinding: T by releasableNotNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = inflater(layoutInflater)
        setContentView(viewBinding.root)
        initialize()
        initListener()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        // viewBinding 为不可空类型，在DonDestroy中通过 release 释放属性代理中get()中value的值
        ::viewBinding.release()
    }

    /**
     * 构建初始化，如果需要
     */
    open fun initialize() {}

    /**
     * 初始化监听
     */
    abstract fun initListener()

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 基于横向的设计图尺寸来进行适配
     */
    override fun screenWidth() = 1920

}