package com.yqh.base.view

import androidx.lifecycle.LifecycleOwner

interface IView : LifecycleOwner {
    fun showLoading(tag: String?)
    fun dismissLoading(tag: String?)
}