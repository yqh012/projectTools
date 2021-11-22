package com.yqh.tv.screen.libannotation

/**
 * 添加该注解可以创建快捷方式
 * 1. 实体转 map
 * 2. map 转 实体
 * 3. contrast 对比内容是否相等(多用于适配器内容比较)
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class Model