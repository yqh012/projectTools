package com.yqh.tv.screen.tools.continuation.test

import com.yqh.tv.screen.tools.continuation.delay
import com.yqh.tv.screen.tools.continuation.launch
import com.yqh.tv.screen.tools.continuation.runBlocking
import com.yqh.tv.screen.tools.continuation.util.log

fun main() {
    runBlocking {
        log(1)
        val job = launch {
            log(4)
            delay(1000)
            log(5)
        }
//        job.invokeOnCompletion {
//
//        }
        log(2)
        job.join()  //等待job执行结束,在执行后面的操作
        delay(1000)
        log(3)

    }
}