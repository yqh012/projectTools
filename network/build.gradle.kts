plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = Version.compileSdk

    defaultConfig {
        minSdk = Version.minSdk
        targetSdk = Version.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Android.core_ktx)
    implementation(Android.appcompat)
    implementation(Android.material)

    api(ThirdParty.okhttp.okhttp)
    api(ThirdParty.retrofit.retrofit)
    api(ThirdParty.retrofit.retrofit_convert_gson)

    implementation(Android.coroutinesCore)
    implementation(Android.coroutinesAndroid)
    implementation(Android.coroutinesJdk)

    testImplementation(Junit.junit)
    androidTestImplementation(Junit.ext)
    androidTestImplementation(Junit.espresso)

}