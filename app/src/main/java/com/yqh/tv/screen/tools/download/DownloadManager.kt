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
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS
import javax.net.ssl.*

object DownloadManager {
    private val downloadDirectory by lazy {
        File(AppUtil.application.filesDir, "download").also { it.mkdirs() }
    }

    init {
        initSSLConfig()
    }

    private fun initSSLConfig() {
        val trustManagers = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) = Unit

            override fun checkServerTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) = Unit

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return emptyArray()
            }
        })

        try {
            val ssl = SSLContext.getInstance("SSL")
            ssl.init(null, trustManagers, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val okhttpClient by lazy {
        OkHttpClient.Builder()
//            .connectTimeout(5, TimeUnit.SECONDS)
//            .readTimeout(5, SECONDS)
//            .writeTimeout(5, SECONDS)
//            .callTimeout(5, SECONDS)
            .build()
    }

    fun download(path: String, name: String): Flow<DownLoadState> {
        val file = File(downloadDirectory, name)
        return flow {
            val request = Request.Builder().get().url(path).build()
            val call = okhttpClient.newCall(request)
            val response = call.execute()
            if (response.isSuccessful) {
                response.body?.let { body ->
                    val total = body.contentLength()
                    var emitValue = 0L
                    file.outputStream().use { output ->
                        body.byteStream().use { input ->
                            input.copyTo(output) { bytesCopied ->
                                val progress = bytesCopied * 100 / total
                                if (progress - emitValue > 5) {
                                    emit(DownLoadState.Progress(progress.toInt()))
                                    emitValue = progress
                                    LogUtils.e("progress", "progress : $progress")
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