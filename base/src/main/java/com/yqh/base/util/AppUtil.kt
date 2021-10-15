package com.yqh.base.util

import android.app.Application

object AppUtil {
    val application: Application? by lazy {
        Class.forName("android.app.ActivityThread")
            .getDeclaredMethod("currentApplication")
            .invoke(null) as Application
    }
}