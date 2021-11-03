package com.yqh.tv.screen.tools.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.yqh.base.SuperActivity

open abstract class BaseActivity<T : ViewBinding>(val inflater: (inflater: LayoutInflater) -> T) :
    SuperActivity() {

    protected lateinit var viewBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = inflater(layoutInflater)
        setContentView(viewBinding.root)

        initialize()
        initListener()
        initData()
    }

    /**
     * 构建初始化，如果需要
     */
    open fun initialize(){}

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