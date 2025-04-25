plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    //plugins for seialization
    kotlin("plugin.serialization") version "1.9.22"
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

    // secrets gradle plugin
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "no.uio.ifi.in2000.vaeraktiv"
    compileSdk = 35

    defaultConfig {
        applicationId = "no.uio.ifi.in2000.vaeraktiv"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.hilt.android.testing)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // lagt til
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.serialization.kotlinx.cbor)
    implementation(libs.ktor.serialization.kotlinx.protobuf)

    // navigation
    implementation(libs.androidx.navigation.compose)

    // materials
    implementation(libs.androidx.material)
    implementation(libs.material)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // google play location services
    implementation(libs.play.services.location)

    // saving favorite locations with json
    implementation(libs.gson)


    // deepseek
//    implementation(libs.deepseek.kotlin)

    // deepseek dependencies (ktor client logging)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.cio)

    // openAI
    implementation(libs.openai.client)

    // google maps places api
    implementation(libs.places)

    // core testing
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)

    // junit testing
    testImplementation(libs.junit)

    // mock testing
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)

    // Needed for testing of final classes
    testImplementation(libs.byte.buddy)
    testImplementation(libs.byte.buddy.agent)

    // hilt testing
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.android.compiler)


}

kapt {
    correctErrorTypes = true
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "local.defaults.properties"
}
