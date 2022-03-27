package com.yqh.tv.screen.tools.continuation.test

import com.yqh.tv.screen.tools.continuation.async
import com.yqh.tv.screen.tools.continuation.delay
import com.yqh.tv.screen.tools.continuation.util.log
import java.lang.IllegalStateException

suspend fun main() {
    val deferred = async {
        log(1)
        delay(1000)
        log(2)
        "hello kotlin"
//        throw IllegalStateException("抛出异常了")
    }

    log(3)
    try {
        val result = deferred.await()
        log(4, result)
        delay(1000)
        log(5)
    } catch (e: Exception) {
        log(6,e.message)
    }


}