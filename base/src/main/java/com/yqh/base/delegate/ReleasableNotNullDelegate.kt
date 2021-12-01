package com.yqh.base.delegate

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

class NullableDelegate<T> : ReadWriteProperty<Any, T> {
    private var value: T? = null
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("代理对象未被初始化...")
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.value = value
    }

    fun release() {
        this.value = null
    }
}

fun <T : Any> releasableNotNull() = NullableDelegate<T>()

fun KProperty0<*>.release() {
    isAccessible = true
    (this.getDelegate() as? NullableDelegate<*>)?.release()
        ?: throw IllegalStateException("release 的代理对象不是 NullableDelegate...")
}