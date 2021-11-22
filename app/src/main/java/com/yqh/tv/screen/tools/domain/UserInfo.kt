package com.yqh.tv.screen.tools.domain

import com.yqh.tv.screen.libannotation.FieldIngore
import com.yqh.tv.screen.libannotation.Model

@Model
data class UserInfo(
    val id: Int,
    var name: String,
    @FieldIngore var age: Int,
    val sex: String
)
