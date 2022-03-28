package com.yqh.tv.screen.tools.download

import java.io.File

sealed class DownLoadState() {
    object None : DownLoadState()
    class Progress(val value: Int) : DownLoadState()
    class Complete(val file: File) : DownLoadState()
    class Error(val error: Throwable) : DownLoadState()
}
