package com.yqh.tv.screen.tools.continuation.test

import androidx.lifecycle.flowWithLifecycle
import com.yqh.tv.screen.tools.continuation.launch
import com.yqh.tv.screen.tools.continuation.util.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.KClass


const val API_URL = "https://api.github.com"

data class Contributor(val login: String? = "", val contributions: Int)

object RetrofitApi {
    val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> create(clazz: Class<T>): T = retrofit.create(clazz)

}

interface GitHub {
    @GET("/repos/{owner}/{repo}/contributors")
    suspend fun contributors(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Call<List<Contributor>>
}

val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
    log("launcher error : ${throwable.message}")
}

val flow = flow {
    emit(1)
    delay(400)
    supervisorScope {
        launch(exceptionHandler) {
            throw NullPointerException("launcher null ...")
        }
    }
    emit(2)
//        throw NullPointerException("null ... ")
    delay(400)
    emit(3)
}.flowOn(Dispatchers.IO)

suspend fun aaa()  {
    val job = CoroutineScope(EmptyCoroutineContext).launch {
//        listOf(flow, flow).merge()
//        flow.flatMapConcat {
//            flow {
//                emit("a")
//                emit("b")
//                emit("c")
//            }
//        }

        flow.catch { throws ->
//        flow.catch { throws ->
            log("error : ${throws.message}")
        }
            .collectLatest {
            log("collectLatest value $it")
            delay(500)
            log("value is $it")
        }

    }
    delay(500)
    job.cancel()
    job.join()
}

suspend fun main() {
    aaa()
//    delay(400)

//    supervisorScope {
//        val call = RetrofitApi.create(GitHub::class.java).contributors("square", "retrofit")
//        val response = call.awaitResponse()
//        println(response)
//    }

}


//suspend fun main() {
//    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
//        log("parentHandler", throwable)
//    }
//
//    val childExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
//        log("childHandler", throwable)
//    }
//
//    val job = GlobalScope.launch(exceptionHandler) {
//        delay(1000)
//        log(1)
//        delay(1000)
//        log(2)
//
//        supervisorScope {
//            val job1 = launch(childExceptionHandler) {
//                throw ArithmeticException("Div 11 ...")
//            }
//
//            job1.join()
//            delay(1000)
//            log(3)
//        }
//
//        delay(1000)
//        log(4)
//
//    }
//    job.join()
//}