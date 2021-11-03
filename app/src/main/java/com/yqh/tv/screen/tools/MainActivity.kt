package com.yqh.tv.screen.tools

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import com.yqh.tv.screen.tools.base.BaseActivity
import com.yqh.tv.screen.tools.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                viewBinding.titleName.text = "横屏"
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                viewBinding.titleName.text = "竖屏"
            }
        }
    }

    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}