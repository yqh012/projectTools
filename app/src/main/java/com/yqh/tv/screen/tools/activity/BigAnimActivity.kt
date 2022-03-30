package com.yqh.tv.screen.tools.activity

import android.widget.Toast
import com.yqh.base.view.clickAnim
import com.yqh.tv.screen.tools.base.BaseActivity
import com.yqh.tv.screen.tools.databinding.ActivityBigAnimLayoutBinding

class BigAnimActivity :
    BaseActivity<ActivityBigAnimLayoutBinding>(ActivityBigAnimLayoutBinding::inflate) {
    override fun initListener() {
        viewBinding.apply {
            viewBinding.buttonOne.let {
                it.clickAnim()
                it.setOnClickListener {
                    toast("buttonOne click...")
                }
                it.setOnLongClickListener {
                    toast("buttonOne long click...")
                    true
                }
            }
            viewBinding.buttonTwo.let {
                it.clickAnim()
                it.setOnClickListener {
                    toast("buttonTwo click...")
                }
                it.setOnLongClickListener {
                    toast("buttonTwo long click...")
                    true
                }
            }
        }
    }

    override fun initData() {

    }

    override fun showLoading(tag: String?) {
//        TODO("Not yet implemented")
    }

    override fun dismissLoading(tag: String?) {
//        TODO("Not yet implemented")
    }

    fun toast(msg: Any) {
        Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show()
    }
}