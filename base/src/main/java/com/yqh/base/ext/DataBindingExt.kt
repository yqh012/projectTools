package com.yqh.base.ext

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * adapter 适配器创建 ViewDataBinding 的快捷方式
 */
inline fun <reified T> ViewGroup.inflate(layoutId: Int): T where T : ViewDataBinding = run {
    DataBindingUtil.inflate(
        LayoutInflater.from(context),
        layoutId,
        this,
        false
    )
}