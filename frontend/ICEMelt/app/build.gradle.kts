plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.map.secret)
}

android {
    namespace = "com.jelhackers.icemelt"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jelhackers.icemelt"
        minSdk = 28
        targetSdk = 35
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
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

apply(plugin = "com.google.gms.google-services")

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.junit)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.firebase.firestore)

    // Firebase BoM manages versions automatically
    implementation(platform("com.google.firebase:firebase-bom:32.2.0"))

    implementation("com.google.firebase:firebase-firestore")

}