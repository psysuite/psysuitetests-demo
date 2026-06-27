plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id("kotlin-parcelize")
}

android {

    namespace = Configs.psysuitetestsnamespace
    compileSdk = Configs.compileSdkVersion

    defaultConfig {
        minSdk = Configs.minSdkVersion
        targetSdk = Configs.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    namespace = "org.albaspazio.psysuite.tests"

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile(ProGuards.proguardTxt), ProGuards.androidDefault)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Core dependencies - psysuitecore already includes nativeaudio
    api(project(":psysuitecore"))
    api(project(":core"))
    api(project(":psysuitepython"))

    // Kotlin
    implementation(Dependencies.Kotlin.stdLib)
    implementation(Dependencies.Kotlin.coroutinesCore)
    implementation(Dependencies.Kotlin.coroutinesAndroid)

    // Android
    implementation(Dependencies.permissions)
    implementation(Dependencies.AndroidX.legacy_support)
    implementation(Dependencies.AndroidX.fragment)
    implementation(Dependencies.AndroidX.lifecycleviewmodel)
    implementation(Dependencies.AndroidX.ktxCore)
    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.AndroidX.recycleView)
    api(Dependencies.AndroidX.preference)

    // Moshi
    implementation(Dependencies.Moshi.moshi)
    implementation(Dependencies.Moshi.moshiKt)

    // Test dependencies
    testImplementation(Dependencies.mockito_core)
    testImplementation(Dependencies.mockito_kotlin)

    // Android test dependencies
    testImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.AndroidX.junitExt)
    androidTestImplementation(Dependencies.AndroidX.testEspressoCore)
}
