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
    }

    /**
     * 基于横向的设计图尺寸来进行适配
     */
    override fun screenWidth() = 1920

}