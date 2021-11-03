package com.yqh.tv.screen.tools.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.yqh.base.SuperFragment

class BaseFragment<T : ViewBinding>(var inflater: (inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean) -> T) :
    SuperFragment() {
    protected lateinit var viewBinding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = this.inflater(inflater, container, false)
        return viewBinding.root
    }

}