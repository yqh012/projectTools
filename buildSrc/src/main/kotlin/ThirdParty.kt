object ThirdParty {

    const val multidex = "com.android.support:multidex:1.0.3"

    const val utilCodex = "com.blankj:utilcodex:1.30.6"

    const val leanback = "androidx.leanback:leanback:1.2.0-alpha01"

    val kotlinpoet = KotlinPoet
    object KotlinPoet {
        private const val kotlin_poet_version = "1.4.3"
        private const val apt_utils_version = "1.7.1"
        const val kotlin_poet = "com.squareup:kotlinpoet:$kotlin_poet_version"
        const val apt_utils = "com.bennyhuo.aptutils:aptutils:$apt_utils_version"
    }

}