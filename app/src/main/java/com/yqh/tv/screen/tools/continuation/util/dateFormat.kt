package com.yqh.tv.screen.tools.continuation.util

import java.text.SimpleDateFormat
import java.util.*

val dateFormat = SimpleDateFormat("HH:mm:ss:SSS")

val now = {
    dateFormat.format(Date(System.currentTimeMillis()))
}

fun log(vararg msg: Any?) =
    println("${now()} [${Thread.currentThread().name}] ${msg.joinToString(" ")}")

fun stackTrace() {
    Throwable().printStackTrace(System.out)
}