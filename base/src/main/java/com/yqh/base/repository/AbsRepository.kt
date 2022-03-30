package com.yqh.base.repository

import com.yqh.tools.network.base.BaseRepository
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

object Repository {
    private val repositoryMap = ConcurrentHashMap<KClass<out AbsRepository>, AbsRepository>()

    fun <T : AbsRepository> KClass<T>.get(): T {
        return repositoryMap[this] as T
    }

    fun AbsRepository.register() {
        repositoryMap[this::class] = this
    }
}

abstract class AbsRepository : BaseRepository() {
    init {
        Repository.run { register() }
    }
}

class RepositoryDelegate<T : AbsRepository>(val kClass: KClass<T>) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        Repository.run { kClass.get() }
}

inline fun <reified T : AbsRepository> repositoryOf(): RepositoryDelegate<T> {
    return RepositoryDelegate(T::class)
}
