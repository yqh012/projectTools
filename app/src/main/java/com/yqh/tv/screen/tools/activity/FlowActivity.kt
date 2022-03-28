package com.yqh.tv.screen.tools.activity

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.yqh.tv.screen.tools.base.BaseActivity
import com.yqh.tv.screen.tools.databinding.ActivityFlowBinding
import com.yqh.tv.screen.tools.dialog
import com.yqh.tv.screen.tools.download.DownLoadState.*
import com.yqh.tv.screen.tools.viewmodel.FlowViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class FlowActivity : BaseActivity<ActivityFlowBinding>(ActivityFlowBinding::inflate) {
    val viewModel: FlowViewModel by viewModels()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        LogUtils.e(throwable)
    }

    override fun initListener() {
        viewBinding.clickDialog.setOnClickListener {
            lifecycleScope.launch {
                val result = dialog("警告", "确定选择吗?")
                ToastUtils.showLong("value  is $result")
            }
        }

        viewBinding.clickError.setOnClickListener {
            lifecycleScope.launch(exceptionHandler) {
//                throw NullPointerException("null ... ")
                supervisorScope {
                    launch {
                        throw NullPointerException("111")
                    }
                    LogUtils.e("hahaha")
                }

                LogUtils.e("lalalala")
            }
        }

        viewBinding.clickDownloadFile.let { button ->
            viewModel.downloadState.observe(this) { state ->
                when (state) {
                    None -> {
                        button.text = "开始下载"
                        button.isEnabled = true
                        button.setOnClickListener {
                            lifecycleScope.launch {
                                viewModel.startDownload(
                                    "https://kotlinlang.org/docs/kotlin-reference.pdf",
                                    "Kotlin-Docs.pdf"
                                )
                            }
                        }
                    }
                    is Progress -> {
                        button.text = "下载中...${state.value}"
                        button.isEnabled = false
                    }
                    is Complete -> {
                        button.text = "下载完成"
                        button.isEnabled = true
                        button.setOnClickListener {
                            ToastUtils.showLong("下载完成 : ${state.file.absolutePath}")
                            state.file.delete()
                        }
                    }
                    is Error -> {
                        ToastUtils.showLong("下载出错 : ${state.error}")
                        LogUtils.e("eee",state.error)
                        button.text = "开始下载"
                        button.isEnabled = true
                        button.setOnClickListener {
                            lifecycleScope.launch {
                                viewModel.startDownload(
                                    "https://kotlinlang.org/docs/kotlin-reference.pdf",
                                    "Kotlin-Docs.pdf"
                                )
                            }
                        }
                    }
                }
            }
        }

    }

    override fun initData() {

    }
}