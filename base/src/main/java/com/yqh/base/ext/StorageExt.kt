package com.yqh.base.ext

import android.os.Parcelable
import com.tencent.mmkv.MMKV

fun save(key: String, value: Int) = MMKV.defaultMMKV().encode(key, value)

fun getInt(key: String, defaultValue: Int = 0) = MMKV.defaultMMKV().decodeInt(key, defaultValue)

fun save(key: String, value: Boolean) = MMKV.defaultMMKV().encode(key, value)

fun getBoolean(key: String, defaultValue: Boolean = false) =
    MMKV.defaultMMKV().decodeBool(key, defaultValue)

fun save(key: String, value: Long) = MMKV.defaultMMKV().encode(key, value)

fun getLong(key: String, defaultValue: Long = 0L) = MMKV.defaultMMKV().decodeLong(key, defaultValue)

fun save(key: String, value: Float) = MMKV.defaultMMKV().encode(key, value)

fun getFloat(key: String, defaultValue: Float = 0f) =
    MMKV.defaultMMKV().decodeFloat(key, defaultValue)

fun save(key: String, value: Double) = MMKV.defaultMMKV().encode(key, value)

fun getDouble(key: String, defaultValue: Double = 0.0) =
    MMKV.defaultMMKV().decodeDouble(key, defaultValue)

fun save(key: String, value: String) = MMKV.defaultMMKV().encode(key, value)

fun getString(key: String, defaultValue: String? = null) =
    MMKV.defaultMMKV().decodeString(key, defaultValue)

fun save(key: String, value: ByteArray) = MMKV.defaultMMKV().encode(key, value)

fun getByteArray(key: String, defaultValue: ByteArray? = null) =
    MMKV.defaultMMKV().decodeBytes(key, defaultValue)

fun save(key: String, value: Parcelable) = MMKV.defaultMMKV().encode(key, value)

inline fun <reified T : Parcelable> getParcelable(key: String, defaultValue: T? = null) =
    MMKV.defaultMMKV().decodeParcelable(key, T::class.java, defaultValue)

fun save(key: String, value: MutableSet<String>) = MMKV.defaultMMKV().encode(key, value)

fun getStringSet(key: String, defaultValue: MutableSet<String>? = null): MutableSet<String>? =
    MMKV.defaultMMKV().decodeStringSet(key, defaultValue)

fun containsKey(key: String) = MMKV.defaultMMKV().containsKey(key)

fun removeValue(key: String) = MMKV.defaultMMKV().removeValueForKey(key)

fun removeValues(key: Array<String>) = MMKV.defaultMMKV().removeValuesForKeys(key)