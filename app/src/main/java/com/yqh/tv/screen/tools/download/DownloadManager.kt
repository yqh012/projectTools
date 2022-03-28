package com.yqh.tv.screen.tools.download

import com.blankj.utilcode.util.LogUtils
import com.yqh.base.util.AppUtil
import com.yqh.tv.screen.tools.exception.HttpException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS

object DownloadManager {
    private val downloadDirectory by lazy {
        File(AppUtil.application.filesDir, "download").also { it.mkdirs() }
    }

    private val okhttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, SECONDS)
            .writeTimeout(5, SECONDS)
            .callTimeout(5, SECONDS)
            .build()
    }

    fun download(path: String, name: String): Flow<DownLoadState> {
        val file = File(downloadDirectory, name)
        return flow {
            val request = Request.Builder().get().url(path).build()
            val call = okhttpClient.newCall(request)
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    val total = body.contentLength()
                    var emitValue = 0L
                    file.outputStream().use { output ->
                        body.byteStream().use { input ->
                            input.copyTo(output) { bytesCopied ->
                                val progress = bytesCopied * 100 / total
                                if (progress - emitValue > 5) {
                                    emit(DownLoadState.Progress(progress.toInt()))
                                    emitValue = progress
                                    LogUtils.e("progress","progress : $progress")
                                }
                            }
                        }
                    }

                } ?: throw NullPointerException("response body is null ... ")
                emit(DownLoadState.Complete(file))
            } else {
                throw HttpException(response)
            }
        }.catch { throws ->
            file.delete()
            emit(DownLoadState.Error(throws))
        }.conflate()
    }

    inline fun InputStream.copyTo(
        out: OutputStream,
        bufferSize: Int = DEFAULT_BUFFER_SIZE,
        block: (Long) -> Unit
    ): Long {
        var bytesCopied: Long = 0
        val buffer = ByteArray(bufferSize)
        var bytes = read(buffer)
        while (bytes >= 0) {
            out.write(buffer, 0, bytes)
            bytesCopied += bytes
            bytes = read(buffer)
            block(bytesCopied)
        }
        return bytesCopied
    }

}