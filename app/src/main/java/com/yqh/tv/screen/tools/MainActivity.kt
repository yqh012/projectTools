package com.yqh.tv.screen.tools

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import com.blankj.utilcode.util.AdaptScreenUtils
import com.yqh.tv.screen.tools.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BuildConfig.DEBUG

        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                toast("竖屏")
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                toast("横屏")
            }
        }


    }

    override fun getResources(): Resources {
        return AdaptScreenUtils.adaptWidth(resources, 1920)
    }


    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}