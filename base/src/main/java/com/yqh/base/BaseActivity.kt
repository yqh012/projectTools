package com.yqh.base

import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.leanback.widget.HorizontalGridView
import androidx.leanback.widget.ListRowHoverCardView
import com.blankj.utilcode.util.AdaptScreenUtils

/**
 * 超级父类，目前提供基础的屏幕适配
 * 默认基于宽度适配
 * 默认适配宽度为 1920
 */
open abstract class SuperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getResources(): Resources {
        return AdaptScreenUtils.adaptWidth(resources, screenWidth())
    }

    open fun screenWidth(): Int = 1920

}