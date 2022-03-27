package com.yqh.tv.screen.tools.activity

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.yqh.tv.screen.tools.base.BaseActivity
import com.yqh.tv.screen.tools.databinding.ActivityFlowBinding
import com.yqh.tv.screen.tools.dialog
import com.yqh.tv.screen.tools.viewmodel.FlowViewModel
import kotlinx.coroutines.launch

class FlowActivity : BaseActivity<ActivityFlowBinding>(ActivityFlowBinding::inflate) {
    val viewModel: FlowViewModel by viewModels()

    override fun initListener() {
        viewBinding.clickDialog.setOnClickListener {
            lifecycleScope.launch {
                val result = dialog("警告", "确定选择吗?")
                ToastUtils.showLong("value  is $result")
            }
        }
    }

    override fun initData() {

    }
}