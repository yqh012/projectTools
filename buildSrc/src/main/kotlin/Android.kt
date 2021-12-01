object Android {

    const val kotlin_version = "1.6.0"

    const val core_ktx = "androidx.core:core-ktx:1.3.2"

    const val appcompat = "androidx.appcompat:appcompat:1.2.0"

    const val material = "com.google.android.material:material:1.3.0"

    const val constraint_layout = "androidx.constraintlayout:constraintlayout:2.0.4"

    //标准库
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    //反射库
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"

    val lifecycle = Lifecycle
    object Lifecycle {
        private const val lifecycle_version = "2.4.0"
        const val viewModelKtx          = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
        const val liveDataKtx           = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
        const val lifecycleRuntime      = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
        const val viewModelSavestate    = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
        const val process               = "androidx.lifecycle:lifecycle-process:$lifecycle_version"
        const val commonJava8           = "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"
        const val lifecycleKapt         = "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
    }

    val activity = Activity
    object Activity {
        private const val activity_version = "1.3.1"
        const val activityKtx       = "androidx.activity:activity-ktx:$activity_version"
    }

    val fragment = Fragment
    object Fragment {
        private const val fragment_version = "1.3.6"
        const val fragmentKtx       = "androidx.fragment:fragment-ktx:$fragment_version"
    }

    //协程版本
    private const val coroutines_version = "1.4.3"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version"
    //协程官方框架扩展依赖包
    const val coroutinesJdk = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutines_version"

}