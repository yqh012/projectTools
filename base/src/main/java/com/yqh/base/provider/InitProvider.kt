package com.yqh.base.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.tencent.mmkv.MMKV
import com.yqh.base.repository.AbsRepository
import com.yqh.base.util.AppUtil
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.jvm.internal.impl.load.kotlin.KotlinClassFinder

class InitProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        initAppUtil()
        initMmkv()
        initRepository()
        return true
    }

    private fun initMmkv() {
        MMKV.initialize(this.context)
    }

    private fun initAppUtil() {
        AppUtil.application.apply {
            Utils.init(this)
            LogUtils.getConfig().apply {
                globalTag = "yqh-app" //日志 tag 前缀
            }
        }
    }

    private fun initRepository() {
        AppUtil
            .application
            .resources
            .assets
            .open("repository.txt")
            .bufferedReader()
            .readLines()
            .toList().forEach {
                LogUtils.i("repository init classPath : $it")
                if (Class.forName(it)
                        .newInstance().javaClass.kotlin.supertypes.joinToString { type ->
                            type.toString()
                        }.contains(AbsRepository::class.qualifiedName.toString())
                ) else throw IllegalStateException("@Repository 注解的不是 AbsRepository 的子类，class : $it")
            }
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        return null
    }

    override fun getType(p0: Uri): String? {
        return null
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        return null
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return 0
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return 0
    }
}