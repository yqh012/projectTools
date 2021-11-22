package com.yqh.tv.screen.tools.domain

import com.yqh.tv.screen.libannotation.Model

@Model
data class UserInfo(val id: Int, var name: String, var age: Int, val sex: String)
