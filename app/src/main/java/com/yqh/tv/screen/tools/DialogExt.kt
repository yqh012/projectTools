package com.yqh.tv.screen.tools

import android.content.Context
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun Context.dialog(title: String, message: String): Boolean =
    suspendCancellableCoroutine { cancellationCoroutine ->

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton("确定") { dialog, _ ->
                dialog.dismiss()
                cancellationCoroutine.resume(true)
            }
            .setPositiveButton("取消") { dialog, _ ->
                dialog.dismiss()
                cancellationCoroutine.resume(false)
            }
            .setOnCancelListener {
                cancellationCoroutine.resume(false)
            }
            .create()
            .also { dialog ->
                cancellationCoroutine.invokeOnCancellation {
                    dialog.dismiss()
                    cancellationCoroutine.resume(false)
                }
            }.show()
    }