package com.yqh.tv.screen.libannotation

/**
 * 声明注解后可以忽略相关 data class中实体对象对比的条件，Ingore则忽略此属性字段
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class FieldIngore
