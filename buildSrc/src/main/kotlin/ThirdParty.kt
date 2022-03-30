object ThirdParty {

    const val multidex = "com.android.support:multidex:2.0.1"

    const val utilCodex = "com.blankj:utilcodex:1.30.6"

    const val leanback = "androidx.leanback:leanback:1.2.0-alpha01"

    val kotlinpoet = KotlinPoet

    object KotlinPoet {
        private const val kotlin_poet_version = "1.4.3"
        private const val apt_utils_version = "1.7.1"
        const val kotlin_poet = "com.squareup:kotlinpoet:$kotlin_poet_version"
        const val apt_utils = "com.bennyhuo.aptutils:aptutils:$apt_utils_version"
    }

    val mmkv = Mmkv

    object Mmkv {
        private const val version = "1.2.11"
        const val mmkv = "com.tencent:mmkv:$version"
    }

    val retrofit = Retrofit

    object Retrofit {
        private const val version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val retrofit_convert_gson = "com.squareup.retrofit2:converter-gson:$version"
    }

    val okhttp = OkHttp

    object OkHttp {
        private const val version = "4.9.3"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
    }

}