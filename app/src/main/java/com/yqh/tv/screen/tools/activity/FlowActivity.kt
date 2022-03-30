package com.yqh.tv.screen.tools.activity

import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.yqh.base.ext.launch
import com.yqh.base.ext.launchAndResult
import com.yqh.base.ext.launchScopeResult
import com.yqh.base.ext.launchSubscribe
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
                        LogUtils.e("eee", state.error)
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


        viewModel.netStateInfo.launchSubscribe(this, Lifecycle.State.STARTED) {
            onSuccess = { log("response : $it") }
            onFailed = { errorCode, errorMsg, exception -> log("onFailed ... code : $errorCode , msg : $errorMsg , exception : $exception") }
            onEmpty = { log("onEmpty ...") }
            onComplete = { log("onComplete ...") }
        }

        viewBinding.getNetInfo.setOnClickListener {
            launch(
                /*viewModel::requestWxArticleInfo("1")*/
                /*{ (viewModel::requestWxArticleInfo)("msg") }*/
                { viewModel.requestWxArticleInfo("msg") },
                { log("loading...") },
                { log("hiddenLoading...") },
                tag = "launchTempTag"
            )
        }

        viewBinding.getNetRealInfo.setOnClickListener {
            launchAndResult(
                { viewModel.requestResultArticleInfo() },
                {
                    onSuccess = { log("response : $it") }
                    onFailed = { errorCode, errorMsg, exception -> log("onFailed ... code : $errorCode , msg : $errorMsg , exception : $exception") }
                    onEmpty = { log("onEmpty ...") }
                    onComplete = { log("onComplete ...") }
                },
                { log("loading...") },
                { log("hiddenLoading...") }
            )
        }

        viewBinding.getNetScopeInfo.setOnClickListener {
//            val exception = CoroutineExceptionHandler{ coroutineContext, throwable ->
//
//            }
            lifecycleScope.launch {
                launchScopeResult(
                    viewModel::requestResultArticleInfo,
                    {
                        onSuccess = { log("response : $it") }
                        onFailed = { errorCode, errorMsg, exception -> log("onFailed ... code : $errorCode , msg : $errorMsg , exception : $exception") }
                        onEmpty = { log("onEmpty ...") }
                        onComplete = { log("onComplete ...") }
                    },
                    { log("loading...") },
                    { log("hiddenLoading...") }
                )
            }
        }

    }

    override fun initData() {

    }

    override fun showLoading(tag: String?) {
        log("showLoading ... tag : $tag")
    }

    override fun dismissLoading(tag: String?) {
        log("dismissLoading ... tag : $tag")
    }

    fun log(msg: Any) {
        LogUtils.d("currentThread : ${Thread.currentThread().name} ; msg : $msg")
    }
}