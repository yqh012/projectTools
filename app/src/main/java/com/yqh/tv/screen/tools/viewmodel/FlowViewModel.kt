package com.yqh.tv.screen.tools.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aisier.bean.WxArticleBean
import com.yqh.base.repository.repositoryOf
import com.yqh.tools.network.eneity.ApiResponse
import com.yqh.tv.screen.tools.download.DownLoadState
import com.yqh.tv.screen.tools.download.DownLoadState.None
import com.yqh.tv.screen.tools.download.DownloadManager
import com.yqh.tv.screen.tools.repository.WxArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn

class FlowViewModel : ViewModel() {

    private val repository: WxArticleRepository by repositoryOf()

    val downloadState = MutableLiveData<DownLoadState>(None)

    private val _netStateInfo = MutableStateFlow<ApiResponse<List<WxArticleBean>>>(ApiResponse())
    val netStateInfo = _netStateInfo.asStateFlow()

    suspend fun startDownload(filePath: String, fileName: String) {
        DownloadManager.download(filePath, fileName)
            .flowOn(Dispatchers.IO)
            .collect {
                downloadState.postValue(it)
            }
    }

    suspend fun requestWxArticleInfo(msg: String) {
        _netStateInfo.value = repository.getArticleInfo()
    }

    suspend fun requestResultArticleInfo() = repository.getArticleInfo()
}