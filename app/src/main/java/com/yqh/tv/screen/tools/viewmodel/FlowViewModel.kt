package com.yqh.tv.screen.tools.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yqh.tv.screen.tools.download.DownLoadState
import com.yqh.tv.screen.tools.download.DownLoadState.None
import com.yqh.tv.screen.tools.download.DownloadManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn

class FlowViewModel : ViewModel() {

    val downloadState = MutableLiveData<DownLoadState>(None)

    suspend fun startDownload(filePath: String, fileName: String) {
        DownloadManager.download(filePath, fileName)
            .flowOn(Dispatchers.IO)
            .collect {
                downloadState.postValue(it)
            }
    }
}