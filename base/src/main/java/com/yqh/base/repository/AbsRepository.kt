package com.yqh.base.repository

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

object Repositorys {
    private val repositoryMap = ConcurrentHashMap<KClass<out AbsRepository>, AbsRepository>()

    fun <T : AbsRepository> KClass<T>.get(): T {
        return repositoryMap[this] as T
    }

    fun AbsRepository.register() {
        repositoryMap[this::class] = this
    }
}

abstract class AbsRepository {
    init {
        Repositorys.run { register() }
    }
}

class RepositoryDelegate<T : AbsRepository>(val kClass: KClass<T>) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        Repositorys.run { kClass.get() }
}

inline fun <reified T : AbsRepository> repositoryOf(): RepositoryDelegate<T> {
    return RepositoryDelegate(T::class)
}
