package com.yqh.tv.screen.tools.domain

import android.os.Parcelable
import com.yqh.tv.screen.libannotation.FieldIngore
import com.yqh.tv.screen.libannotation.Model
import kotlinx.android.parcel.Parcelize

@Parcelize
@Model
data class UserInfo(
    val id: Int,
    var name: String,
    @FieldIngore var age: Int,
    val sex: String
):Parcelable
